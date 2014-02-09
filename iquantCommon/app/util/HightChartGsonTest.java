package util;

import java.util.Date;

/**
 * 对HightChartGson的测试
 * User: wenzhihong
 * Date: 12-11-19
 * Time: 上午8:33
 */
public class HightChartGsonTest {
    public static void main(String[] args) {
            test();
        }

        public static void test() {
            TestBean bean = new TestBean();
            bean.setName("wenzhi");
            bean.setPwd("123");
            Date d = new Date();
            bean.setBirthday(new HighChartDataType.HCDate(d));
            String s = HighChartDataType.toJsonWithHighChartDataType(bean);
            System.out.println(s);
        }

        static class TestBean {
            private String name;
            private String pwd;
            private HighChartDataType.HCDate birthday;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPwd() {
                return pwd;
            }

            public void setPwd(String pwd) {
                this.pwd = pwd;
            }

            public HighChartDataType.HCDate getBirthday() {
                return birthday;
            }

            public void setBirthday(HighChartDataType.HCDate birthday) {
                this.birthday = birthday;
            }
        }
}
