package websocket;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @Author binbin
 * @Date 2024 04 23 10 30
 **/
public class Test {
    public static void main(String[] args) {
        System.out.println("开始检测运行环境的安全等级...");
//
        // 检测操作系统信息
        detectOS();
//
//        // 检测网络信息
//        detectNetwork();
//
//        System.out.println("安全等级检测完成。");
        System.out.println("www.baidu.com,是否未公网ip：" + isPublicNetwork("10.206.73.154"));
    }

    // 检测操作系统信息
    private static void detectOS() {
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");

        System.out.println("操作系统： " + osName + " " + osArch + " 版本：" + osVersion);
    }

    // 检测网络信息
    private static void detectNetwork() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                System.out.println("网络接口：" + networkInterface.getName());
                System.out.println("  硬件地址：" + getHardwareAddress(networkInterface));
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    System.out.println("  IP地址：" + address.getHostAddress());
//                    if (isPublicNetwork(address)) {
//                        System.out.println("展示名：" + networkInterface.getDisplayName() + "是否为公共网络：是");
//                    } else {
//                        System.out.println("展示名：" + networkInterface.getDisplayName() + "是否为公共网络：否");
//                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    // 获取硬件地址
    private static String getHardwareAddress(NetworkInterface networkInterface) {
        try {
            byte[] hardwareAddress = networkInterface.getHardwareAddress();
            if (hardwareAddress != null) {
                StringBuilder sb = new StringBuilder();
                for (byte b : hardwareAddress) {
                    sb.append(String.format("%02X", b));
                    sb.append(":");
                }
                sb.deleteCharAt(sb.length() - 1); // 删除最后一个冒号
                return sb.toString();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "未知";
    }


    // 检测是否连接到公共网络
    //https://blog.51cto.com/u_16175500/8297577
    private static boolean isPublicNetwork(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            if (inetAddress.isSiteLocalAddress()) {
                return false;
              //  System.out.println(ipAddress + " 是内网 IP 地址");
            } else {
                return true;
             //   System.out.println(ipAddress + " 是公网 IP 地址");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
