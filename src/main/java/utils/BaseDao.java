package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * describe  工具类，抽取出一些相同的代码
 *BaseDao4 大家也不需要自己写，以后都是复制代码的，如果有时间的同学就写，没时间不用写。
 * 作者：曾昭武
 */
public class BaseDao {

    //这些通常是私有属性
    private  static  String sbqDriver;
    private  static  String sbqUrl;
    private  static  String user;
    private  static  String pwd;


    static {
        init();
    }

    private static void init() {
        System.out.println(111111);
        //在这里面，给上面的属性赋值,值从哪里来?
        //这个代码，不需要大家自己写，会复制就可以
        //1.创建Properties
        Properties psSbq = new Properties();
        //2.拿到文件路径  sbq.properties 是在src目录下
        String path="db.properties";
        //3.通过输入流读取sbq.properties
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream(path);
        //把值加载到 Properties对象中
        try {
            psSbq.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("我要测试");
        //通过键去获取值
        sbqDriver= psSbq.getProperty("sbqDriver.....aaaa");
        System.out.println(sbqDriver);//请大家一定要输出测试一下，有没有值
        sbqUrl=psSbq.getProperty("db.url");
        user=psSbq.getProperty("db.user1");
        pwd=psSbq.getProperty("db.pwd");

    }

    /**
     * 封装建立连接的代码
     */
    public static   Connection getConnection(){
        Connection conn=null;
        try {
            //1.加载驱动  DriverManager
            Class.forName(sbqDriver);
            //2.建立连接  Connection
            conn = DriverManager.getConnection(sbqUrl,user,pwd);
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    //封装释放资源的代码
    public static void closeAll(Connection conn, Statement pstm, ResultSet rs){
        try {
            //如果不判断，会报空指针
            if(rs!=null){
                rs.close();
            }

            if(pstm!=null){
                pstm.close();
            }

            if(conn!=null){
                conn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 封装增删改 现在主要是讲思路，以后这种会让我们写吗？不会
     * 现在封装好之后，DML就是一句话，以后使用框架也是很简单的。
     */
    /**
     *
     * @param sqlSbq  每次传递过来的sql语句
     * @param sbq Object sbq[]接收的参数
     * @return
     */
    public static int executeUpdateSbq(String sqlSbq,Object sbq[]){
        int num = 0;
        Connection conn =null;
        PreparedStatement pstm =null;
        try {
            //1.建立连接
            conn=BaseDao.getConnection();
            //2.处理预编译sql语句   我这里不关心什么sql语句，这是调调用谁提供
            pstm=conn.prepareStatement(sqlSbq);

            //我知道有几个？没有，不知道
            //3.循环遍历
            for(int i =0;i<sbq.length;i++){
                //必需给？赋值
                pstm.setObject(i+1,sbq[i]);
            }

            //4.执行预编译sql语句
            num=pstm.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放资源
            BaseDao.closeAll(conn,pstm,null);
        }

        return num;
    }


    /**
     *
     * @param args
     */
    //如果封装好了之后，各位同学一定要这样测试
    public static void main(String[] args) {
        //请各位同学一定要测试，很多同学就是这里没测试，报错
        System.out.println(BaseDao.getConnection());

    }




}
