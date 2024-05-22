package com.lcp.core.l0217;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;

@Slf4j
@Component
public class LicenseClientManagerService {

    private LicenseModel licenseModel;

    @Value("${custom.register-code:}")
    private String registerCode;

    public Boolean isLinux() {
        String os = System.getProperty("os.name");
        //log.info("os.name: {}", os);
        return !os.toLowerCase().startsWith("win");
    }

    @SneakyThrows
    public String getRegisterCodeFromClient() {
        if (registerCode == null) {
            registerCode = "";
        }
        String fileName = "license.txt";
        if (Files.exists(Paths.get(fileName))) {
            String content = Files.readString(Paths.get(fileName));
            registerCode = content;
        }
        return registerCode.replace(" ", "");
    }

    /**
     * 获取本机申请码
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public String getUniqueCode2() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String code = getUniqueCode();
        return RSAClientService.publicEncrypt(code, RSAClientService.getPublicKey());
    }

    /**
     * 检验本地验证码是否正确
     *
     * @param registerCode
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public void checkRegisterCode2(String registerCode) throws NoSuchAlgorithmException, InvalidKeySpecException {
        checkRegisterCode(RSAClientService.publicDecrypt(registerCode, RSAClientService.getPublicKey()));
    }

    public LicenseModel getLicense() {
        return JSON.parseObject(JSON.toJSONString(this.licenseModel), LicenseModel.class);
    }

    private String getMACAddressByLinux() throws Exception {
        String[] cmd = {"ifconfig"};
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String str1 = sb.toString();
        String str2 = str1.split("ether")[1].trim();
        String result = str2.split("txqueuelen")[0].trim();
        //log.info("Linux MacAddress is: {}", result);
        br.close();
        return result;
    }

    private String getIdentifierByLinux() throws Exception {
        String[] cmd = {"fdisk", "-l"};
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String str1 = sb.toString();
        String str2 = str1.split("identifier:")[1].trim();
        String result = str2.split("Device Boot")[0].trim();
        //log.info("Linux Identifier is: {}", result);
        br.close();
        return result;
    }

    private String getMACAddressByWindows() throws Exception {
        String result = "";
        Process process = Runtime.getRuntime().exec("ipconfig /all");
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        int index = -1;
        while ((line = br.readLine()) != null) {
            index = line.toLowerCase().indexOf("物理地址");
            if (index >= 0) {// 找到了
                index = line.indexOf(":");
                if (index >= 0) {
                    result = line.substring(index + 1).trim();
                }
                break;
            }
        }
        br.close();
        result = result.replace("-", "");
        //log.info("Windows MACAddress is: {}", result);
        return result;
    }

    private String getIdentifierByWindows() throws Exception {
        String result = "";
        Process process = Runtime.getRuntime().exec("cmd /c dir C:");
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.indexOf("卷的序列号是 ") != -1) {
                result = line.substring(line.indexOf("卷的序列号是 ") + "卷的序列号是 ".length(), line.length());
                break;
            }
        }
        br.close();
        result = result.replace("-", "");
        result = result + "00000000";
        result = result.substring(0, 8);
        //log.info("Windows Identifier is: {}", result);
        if (result.equals("00000000")) {
            throw new Exception("硬盘码获取失败，请联系管理员");
        }
        return result;
    }

    @SneakyThrows
    private String getUniqueCode() {
        String macCode = "", diskCode = "";
        try {
            if (!isLinux()) {
                diskCode = getIdentifierByWindows();
                macCode = getMACAddressByWindows();
            } else {
                diskCode = getIdentifierByLinux();
                macCode = getMACAddressByLinux();
            }
            System.out.println("diskCode is " + diskCode);
            System.out.println("macCode is " + macCode);
        } catch (Exception ex) {
            System.out.println("获取申请码错误" + ex.getMessage());
        }
        StringBuilder uniqueCode = new StringBuilder();
        char[] c1 = diskCode.substring(1, 7).toCharArray();
        char[] c2 = macCode.substring(3, 9).toCharArray();
        for (int i = 5; i >= 0; i--) {
            uniqueCode.append(c1[i]).append(c2[i]);
        }
        return uniqueCode.toString();
    }


    @SneakyThrows
    private void checkRegisterCode(String registerCode) {
        if (registerCode == null || registerCode.length() != 21) {
            throw new Exception("注册码格式不正确，长度必须位19位");
        }
        char[] registerCodes = registerCode.toCharArray();
        char[] applyCodes = new char[12];
        char[] validityDate = new char[6];
        char mode = registerCodes[14];
        char[] appCounts = new char[2];
        appCounts[0] = registerCodes[17];
        appCounts[1] = registerCodes[18];
        validityDate[0] = registerCodes[9];
        validityDate[1] = registerCodes[4];
        validityDate[2] = registerCodes[0];
        validityDate[3] = registerCodes[2];
        validityDate[4] = registerCodes[1];
        validityDate[5] = registerCodes[7];
        applyCodes[0] = registerCodes[3];
        applyCodes[1] = registerCodes[5];
        applyCodes[2] = registerCodes[6];
        applyCodes[3] = registerCodes[8];
        applyCodes[4] = registerCodes[10];
        applyCodes[5] = registerCodes[11];
        applyCodes[6] = registerCodes[12];
        applyCodes[7] = registerCodes[13];
        applyCodes[8] = registerCodes[15];
        applyCodes[9] = registerCodes[16];
        applyCodes[10] = registerCodes[19];
        applyCodes[11] = registerCodes[20];
        String currentApplyCode = getUniqueCode();
        String registerApplyCode = String.valueOf(applyCodes);
        String registerValidate = decodeDate(String.valueOf(validityDate));

        if (!currentApplyCode.equals(registerApplyCode)) {
            throw new Exception("注册码错误1");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String nowStr = sdf.format(System.currentTimeMillis());
        if (nowStr.compareTo(registerValidate) > 0) {
            throw new Exception("注册码错误2");
        }
        Integer appCount = Ten2ThirtySix.ThirtysixToDeciaml(String.valueOf(appCounts));
        if (appCount < 0 || appCount > 1295) {
            throw new Exception("注册码错误3");
        }
        licenseModel = new LicenseModel();
        licenseModel.setMode(String.valueOf(mode));
        licenseModel.setValidate(registerValidate);
        licenseModel.setAppCount(appCount);
    }

    private String decodeDate(String validityDateStr) {
        char[] validityDate = validityDateStr.toCharArray();
        char[] validityDate2 = validityDateStr.toCharArray();
        for (int i = 0; i < 6; i++) {
            if (validityDate[i] == '0') {
                validityDate2[i] = '9';
            } else if (validityDate[i] == '1') {
                validityDate2[i] = '8';
            } else if (validityDate[i] == '2') {
                validityDate2[i] = '7';
            } else if (validityDate[i] == '3') {
                validityDate2[i] = '6';
            } else if (validityDate[i] == '4') {
                validityDate2[i] = '5';
            } else if (validityDate[i] == '5') {
                validityDate2[i] = '4';
            } else if (validityDate[i] == '6') {
                validityDate2[i] = '3';
            } else if (validityDate[i] == '7') {
                validityDate2[i] = '2';
            } else if (validityDate[i] == '8') {
                validityDate2[i] = '1';
            } else if (validityDate[i] == '9') {
                validityDate2[i] = '0';
            }
        }
        return String.valueOf(validityDate2);
    }
}
