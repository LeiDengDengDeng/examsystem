package com.exam.service.impl;

import com.exam.bean.Role;
import com.exam.bean.User;
import com.exam.dao.RoleDao;
import com.exam.dao.UserDao;
import com.exam.service.UserService;
import com.exam.util.ExcelUtil;
import com.exam.util.MailUtil;
import com.exam.vo.UserVO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by deng on 2017/10/16.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MailUtil mailUtil;


    @Override
    public boolean auth(String username, String password) {
        User user = userDao.getUser(username);
        if (user == null || !user.getPassword().equals(password)) {
            return false;
        }
        return true;
    }

    @Override
    public UserVO getUserInfo(String username) throws Exception {
        User user = userDao.getUser(username);
        if (user == null) {
            throw new Exception("用户信息不存在");
        }
        return new UserVO(user.getEmail(), roleDao.getRole(user.getRoleId()).getRole(), user.getName(), user.getCode());
    }

    @Override
    public void register(User user) throws Exception {
        User checkUser = userDao.getUser(user.getEmail());
        if (checkUser != null) {
            throw new Exception("用户邮箱重复");
        }
        String content="您已成功完成注册";
        mailUtil.sendEmail(user.getEmail(), "考试网注册通知", content, true);
        userDao.insertUser(user);
    }

    @Override
    public List<UserVO> getUserByGroup(int groupId) {
        List<User> users = userDao.getUserByGroup(groupId);
        List<UserVO>  userVOs = new ArrayList<>();
        for (User item:users){
            Role role=roleDao.getRole(1);
            userVOs.add(new UserVO(item.getEmail(),role.getRole(),item.getName(),item.getCode()));
        }
        return userVOs;

    }

    @Override
    public void insertUserTOGroup(int groupId, String email) {
        userDao.insertUserTOGroup(groupId, email);
    }

    @Override
    public void importUsers(int groupId, String fileCompletePath) throws Exception {
        Workbook wb = ExcelUtil.getWorkbook(fileCompletePath);
        if (wb == null) {
            return;
        }
        Sheet sheet = wb.getSheetAt(0); // 默认表单只有一页
        // 读取每行表,第一行为表头,因此忽略
        for (int j = 1; j < sheet.getLastRowNum() + 1; j++) {
            Row row = sheet.getRow(j);
            if (row == null) {
                continue;
            }
            String email = ExcelUtil.getCellVal(row.getCell(0));
            if (userDao.getUser(email) == null) {
                //随机生成8位密码
                String password = getRandomString(8);
                User user = new User(email, password, 1, "unnamed",0);
                userDao.insertUser(user);
                String content = "系统已为您自动创建用户，用户名为该邮箱，密码为【" + password + "】。";
                mailUtil.sendEmail(email, "考试网注册通知", content, true);
            }

           try {
               userDao.insertUserTOGroup(groupId, email);
           }catch (Exception e){
                continue;
           }
        }
    }

    @Override
    public List<UserVO> getAllStudents() {
        List<User> users=userDao.getAllStudents();
        List<UserVO> list=new ArrayList<>();
        for (User item:users){
            Role role=roleDao.getRole(1);
            list.add(new UserVO(item.getEmail(),role.getRole(),item.getName(),item.getCode()));
        }

        return list;
    }

    @Override
    public void dropStudentFromGroup(int groupId, String email) {
        userDao.dropStudentFromGroup(groupId,email);
    }

    /**
     * 随机生成密码
     *
     * @param length
     * @return
     */
    private static String getRandomString(int length) {
        String base = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }



}
