package com.dale.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dale.health.constant.RedisConstant;
import com.dale.health.dao.CheckGroupDao;
import com.dale.health.dao.CheckItemDao;
import com.dale.health.dao.SetMealDao;
import com.dale.health.entity.PageResult;
import com.dale.health.entity.QueryPageBean;
import com.dale.health.pojo.CheckGroup;
import com.dale.health.pojo.CheckItem;
import com.dale.health.pojo.Setmeal;
import com.dale.health.service.SetMealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

//    @Value("${freemarker.path}")
    private String outPutPath = "D:\\test_project\\health\\health_parent\\health_mobile\\src\\main\\webapp\\pages";
    @Autowired
    private CheckItemDao checkItemDao;
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public List<CheckGroup> getAllCheckgroup() {
        try {
            return checkGroupDao.selectAllCheckgroup();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        try {
            Integer currentPage = queryPageBean.getCurrentPage();
            Integer pageSize = queryPageBean.getPageSize();
            String queryString = queryPageBean.getQueryString();

            PageHelper.startPage(currentPage, pageSize);
            Page<CheckGroup> page = setMealDao.findPage(queryString);

            long total = page.getTotal();
            List<CheckGroup> result = page.getResult();
            return new PageResult(total, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addSetmeal(Setmeal setmeal, List<Integer> ids) {
        Jedis jedis = null;
        try {
            setMealDao.addSetmeal(setmeal);
            if (ids != null && ids.size() > 0) {
                Integer setmealId = setmeal.getId();
                // 保存到redis，便于删除垃圾图片
                jedis = jedisPool.getResource();
                jedis.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
                // setMealDao.deleteCheckgroupBySetmealId(setmealId);
                setMealDao.matchCheckgroupWithSetmeal(setmealId, ids);

                // 使用freemarker生成静态页面
                generateMobileStaticHtml();
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            assert jedis != null;
            jedis.close();
        }
    }

    public void generateMobileStaticHtml() throws TemplateException, IOException {
        List<Setmeal> setmeals = setMealDao.selectAllSetmeal();
        generateMobileSetmealListHtml(setmeals);
        generateMobileSetmealDetailHtml(setmeals);
    }

    public void generateMobileSetmealListHtml(List<Setmeal> setmeals) throws TemplateException, IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("setmealList", setmeals);
        generateHtml("mobile_setmeal.ftl", "m_setmeal.html", map);
    }

    public void generateMobileSetmealDetailHtml(List<Setmeal> setmeals) throws TemplateException, IOException {
        for (Setmeal setmeal : setmeals) {
            Map<String, Object> map = new HashMap<>();
            Setmeal setmeal1 = setMealDao.selectDetailById(setmeal.getId());
            map.put("setmeal", setmeal1);
            generateHtml("mobile_setmeal_detail.ftl", "setmeal_detail_" + setmeal.getId() + ".html", map);
        }
    }

    public void generateHtml(String templateName, String htmlName, Map<String, Object> map) throws IOException, TemplateException {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate(templateName);
        FileWriter fileWriter = new FileWriter(new File(outPutPath + "/" + htmlName));
        template.process(map, fileWriter);
        fileWriter.close();
    }

    @Override
    public Map<String, Object> selectById(Integer id) {
        try {
            Setmeal setmeal = setMealDao.selectById(id);
            List<Integer> ids = checkGroupDao.selectCheckgroupBySetmealId(id);
            Map<String, Object> map = new HashMap<>();
            map.put("setmeal", setmeal);
            map.put("checkgroupIds", ids);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void editSetmeal(Setmeal setmeal, List<Integer> ids) {
        try {
            setMealDao.editSetmeal(setmeal);
            if (ids != null && ids.size() > 0) {
                Integer setmealId = setmeal.getId();
                setMealDao.deleteCheckgroupBySetmealId(setmealId);
                setMealDao.matchCheckgroupWithSetmeal(setmealId, ids);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public List<Setmeal> getAllSetmeal() {
        return setMealDao.selectAllSetmeal();
    }

    /**
     * 使用mybatis的关联查询进行setmeal详细信息的查询
     * @param id
     * @return
     */
    public Setmeal getSetmealDetailById(Integer id) {
        return setMealDao.selectDetailById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setMealDao.selectSetmealCount();
    }

    /**
     * 不使用mybatis的关联查询进行setmeal详细信息的查询
     * @param id
     * @return
     */
    @Override
    public Setmeal findSetmealDetailById(Integer id) {
        // 根据id查询setmeal的信息
        Setmeal setmeal = setMealDao.selectById(id);
        if (setmeal != null) {
            // 根据setmealId查询关联的checkgroup的id
            List<Integer> checkgroupIds = checkGroupDao.selectCheckgroupBySetmealId(id);
            if (checkgroupIds != null && checkgroupIds.size() > 0) {
                // 根据checkgroup的id查询每一个checkgroup的信息
                List<CheckGroup> checkGroupList = getCheckgroupList(checkgroupIds);
                setmeal.setCheckGroups(checkGroupList);
            } else {
                throw new RuntimeException("该setmeal不关联任何checkgroup，id " + id);
            }
        } else {
            throw new RuntimeException("setmeal不存在，id " + id);
        }
        return setmeal;
    }

    public List<CheckGroup> getCheckgroupList(List<Integer> checkgroupIds) {
        List<CheckGroup> checkGroupList = new ArrayList<>();
        checkgroupIds.forEach(checkgroupId -> {
            // 查询checkgroup的信息
            CheckGroup checkGroup = checkGroupDao.selectById(checkgroupId);
            if (checkGroup != null) {
                // 根据checkgroupId查询关联的checkitem的id
                List<Integer> checkitemIds = checkGroupDao.selectCheckitemByCheckgroupId(checkgroupId);
                // 根据checkitemId查询每一个checkitem的信息，并封装到对应的checkgroup中
                if (checkitemIds != null && checkgroupIds.size() > 0) {
                    List<CheckItem> checkItemList = getCheckItemList(checkitemIds);
                    checkGroup.setCheckItems(checkItemList);
                } else {
                    throw new RuntimeException("该checkgroup不关联任何checkitem，id: " + checkgroupId);
                }
                checkGroupList.add(checkGroup);
            } else {
                throw new RuntimeException("checkgroup不存在，id: " + checkgroupId);
            }
        });
        return checkGroupList;
    }

    public List<CheckItem> getCheckItemList(List<Integer> checkitemIds) {
        List<CheckItem> checkItemList = new ArrayList<>();
        checkitemIds.forEach(checkitemId -> {
            CheckItem checkItem = checkItemDao.selectCheckitemById(checkitemId);
            if (checkItem == null) throw new RuntimeException("checkitem不存在，id: " + checkitemId);
            checkItemList.add(checkItem);
        });
        return checkItemList;
    }

}
