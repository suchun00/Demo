package android.hardware.uhf.magic;

/**
 * Created by Administrator on 2017/10/26.
 */

import java.util.regex.Pattern;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

//import android.util.Log; //mifarereader
public class reader {
    static public Handler m_handler = null;
    static Boolean m_bASYC = false, m_bLoop = false, m_bOK = false;
    static byte[] m_buf = new byte[10240];
    static int m_nCount = 0, m_nReSend = 0, m_nread = 0;
    static int msound = 0;
    static SoundPool mSoundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);;
    static public String m_strPCEPC = "";

    /**
     * 初始化设备
     *
     * @param strpath
     */
    static public native void init(String strpath);

    /**
     * 打开设备
     *
     * @param strpath
     *            设备地址CM719是"//dev//ttyS4"
     * @return 成功返回0，失败返回错误标志（-20产品不对，-1设备无法打开，1设备已打开， -2设备参数无法设置）
     */
    static public native int Open(String strpath);

    /**
     * 读数据
     *
     * @param pout
     *            存放读到得数据
     * @param nStart
     *            存放数据在pout的起始位置
     * @param nCount
     *            想读到得数据长度
     * @return
     */
    static public native int Read(byte[] pout, int nStart, int nCount);

    /**
     * 关闭设备
     */
    static public native void Close();

    /**
     * 清楚设备缓存中数据
     */
    static public native void Clean();

    /**
     * 单次轮询
     *
     * @return 成功返回0x10,错误返回0x11
     */
    static public native int Inventory();

    /**
     * 多次轮询
     *
     * @param ntimes
     *            轮询次数，轮询次数限制为 0-65535次
     * @return 成功返回0x10,错误返回0x11
     */
    static public native int MultiInventory(int ntimes);

    /**
     * 停止多次轮询
     *
     * @return 成功返回0x10,错误返回0x11
     */
    static public native int StopMultiInventory();

    /**
     * 设置Select参数，并且设置在单次轮询或多次轮询 Inventory之前，先发送 Select指令。在多标签的情况下， 可以只对特定标签进行轮询
     * Inventory操作。
     *
     * @param selPa
     *            参数(Target: 3b 000, Action: 3b 000, MemBank: 2b 01)
     * @param nPTR
     *            (以 bit为单位，非word)从 PC和 EPC存储位开始
     * @param nMaskLen
     *            Mask长度
     * @param turncate
     *            (0x00是Disable truncation，0x80是 Enable truncation)
     * @param mask
     * @return 成功返回0x00,错误返回非0
     */
    static public native int Select(byte selPa, int nPTR, byte nMaskLen,
                                    byte turncate, byte[] mask);

    /**
     * 设置发 送Select 指令
     *
     * @param data
     *            (0x01是取消 Select指令，0x00是发 Select 指令)
     * @return 成功返回0x00,错误返回非0
     */
    static public native int SetSelect(byte data);

    /**
     * 读标签数据存储区
     *
     * @param password
     *            读密码，4个字节
     * @param nUL
     *            PC+EPC长度
     * @param PCandEPC
     *            PC+EPC数据
     * @param membank
     *            标签数据存储区
     * @param nSA
     *            读标签数据区地址偏移
     * @param nDL
     *            读标签数据区地址长度
     * @return
     */
    static public native int ReadLable(byte[] password, int nUL,
                                       byte[] PCandEPC, byte membank, int nSA, int nDL);

    /**
     * 写标签数据存储区
     *
     * @param password
     *            密码4字节
     * @param nUL
     *            PC+EPC长度
     * @param PCandEPC
     *            PC+EPC数据
     * @param membank
     *            标签数据存储区
     * @param nSA
     *            写标签数据区地址偏移
     * @param nDL
     *            写标签数据区数据长度
     * @param data
     *            写入数据
     * @return
     */
    static public native int WriteLable(byte[] password, int nUL,
                                        byte[] PCandEPC, byte membank, int nSA, int nDL, byte[] data);

    /**
     * 对单个标签，锁定 Lock 或者解锁 Unlock 该标签的数据存储区
     *
     * @param password
     *            锁定密码
     * @param nUL
     *            PC+EPC长度
     * @param PCandEPC
     *            PC+EPC数据
     * @param nLD
     *            锁定还是解锁命令
     * @return
     */

    static public native int Lock(byte[] password, int nUL, byte[] PCandEPC,
                                  int nLD);

    /**
     * 灭活 Kill 标签
     *
     * @param password
     *            密码
     * @param nUL
     *            PC+EPC长度
     * @param EPC
     *            PC+EPC内容
     * @return
     */
    static public native int Kill(byte[] password, int nUL, byte[] EPC);

    /**
     * 灭活标签 （结果通过Handle异步发送）
     *
     *
     *            读写器地址
     *
     *            销毁密码（4个字节）
     * @return
     */

    static public int KillLables(byte[] password, int nUL, byte[] EPC) {
        Clean();
        int nret = Kill(password, nUL, EPC);
        if (!m_bASYC) {
            StartASYCKilllables();
        }
        return nret;
    }

    /**
     * 获取参数
     *
     * @return
     */
    static public native int Query();

    /**
     * 设置Query命令中的相关参数
     *
     * @param nParam
     *            参数为 2字节，有下面的具体参数按位拼接而成： DR(1 bit): DR=8(1b0), DR=64/3(1b1).
     *            只支持 DR=8的模式 M(2 bit): M=1(2b00), M=2(2b01), M=4(2b10),
     *            M=8(2b11). 只支持 M=1的模式 TRext(1 bit): No pilot tone(1b0), Use
     *            pilot tone(1b1). 只支持 Use pilot tone(1b1)模式 Sel(2 bit):
     *            ALL(2b00/2b01), ~SL(2b10), SL(2b11) Session(2 bit): S0(2b00),
     *            S1(2b01), S2(2b10), S3(2b11) Target(1 bit): A(1b0), B(1b1) Q(4
     *            bit): 4b0000-4b1111
     * @return
     */
    static public native int SetQuery(int nParam);

    /**
     * 设置工作地区频段
     *
     * @param region
     *            Region Parameter 01 中国900MHz 04 中国800MHz 02 美国 03 欧洲 06 韩国
     * @return
     */
    static public native int SetFrequency(byte region);

    /**
     * 设置工作信道
     *
     * @param channel
     *            中国900MHz 信道参数计算公式，Freq_CH 为信道频率： CH_Index =
     *            (Freq_CH-920.125M)/0.25M
     *
     *            中国800MHz 信道参数计算公式，Freq_CH 为信道频率： CH_Index =
     *            (Freq_CH-840.125M)/0.25M
     *
     *            美国信道参数计算公式，Freq_CH为信道频率： CH_Index = (Freq_CH-902.25M)/0.5M
     *
     *            欧洲信道参数计算公式，Freq_CH为信道频率： CH_Index = (Freq_CH-865.1M)/0.2M
     *
     *            韩国信道参数计算公式，Freq_CH为信道频率： CH_Index = (Freq_CH-917.1M)/0.2M
     * @return
     */
    static public native int SetChannel(byte channel);

    /**
     * 获取工作信道
     *
     * @return 中国900MHz 信道参数计算公式，Freq_CH 为信道频率： Freq_CH = CH_Index * 0.25M +
     *         920.125M
     *
     *         中国800MHz 信道参数计算公式，Freq_CH 为信道频率： Freq_CH = CH_Index * 0.25M +
     *         840.125M
     *
     *         美国信道参数计算公式，Freq_CH为信道频率： Freq_CH = CH_Index * 0.5M + 902.25M
     *
     *         欧洲信道参数计算公式，Freq_CH为信道频率： Freq_CH = CH_Index * 0.2M + 865.1M
     */
    static public native int GetChannel();

    /**
     * 设置为自动跳频模式或者取消自动跳频模式
     *
     * @param auto
     *            0xFF 为设置自动跳频，0x00为取消自动跳频
     * @return
     */
    static public native int SetAutoFrequencyHopping(byte auto);

    /**
     * 获取发射功率
     *
     * @return 返回实际发射功率
     */
    static public native int GetTransmissionPower();

    /**
     * 设置发射功率
     *
     * @param nPower
     *            发射功率
     * @return
     */
    static public native int SetTransmissionPower(int nPower);

    /**
     * 设置发射连续载波或者关闭连续载波
     *
     * @param bOn
     *            0xFF 为打开连续波，0x00为关闭连续波
     * @return
     */
    static public native int SetContinuousCarrier(byte bOn);

    /**
     * 获取当前读写器接收解调器参数
     *
     * @param bufout
     *            两个字节，第一个混频器增益，第二个中频放大器增益 混频器 Mixer增益表 Type Mixer_G(dB) 0x00 0
     *            0x01 3 0x02 6 0x03 9 0x04 12 0x05 15 0x06 16 中频放大器 IF AMP增益表
     *            Type IF_G(dB) 0x00 12 0x01 18 0x02 21 0x03 24 0x04 27 0x05 30
     *            0x06 36 0x07 40
     * @return
     */
    static public native int GetParameter(byte[] bufout);

    /**
     * 设置当前读写器接收解调器参数
     *
     * @param bMixer
     *            混频器增益
     * @param bIF
     *            中频放大器增益
     * @param nThrd
     *            信号调节阀值,信号解调阈值越小能解调的标签返回RSSI越低，但越不稳定，低于一
     *            定值完全不能解调；相反阈值越大能解调的标签返回信号 RSSI越大，距离越近，越稳定。0x01B0是推荐的 最小值
     * @return
     */
    static public native int SetParameter(byte bMixer, byte bIF, int nThrd);

    /**
     * 测试射频输入端阻塞信号
     *
     * @param bufout
     * @return
     */
    static public native int ScanJammer(byte[] bufout);

    /**
     * 测试射频输入端 RSSI信号大小，用于检测当前环境下有无读写器在工作
     *
     * @param bufout
     * @return
     */
    static public native int TestRssi(byte[] bufout);

    /**
     * 设置IO端口的方向，读取 IO电平以及设置 IO电平
     *
     * @param p1
     * @param p2
     * @param p3
     *            编号 描述 长度 说明 0 参数0 1 byte 操作类型选择: 0x00：设置 IO 方向； 0x01：设置 IO 电平；
     *            0x02：读取 IO 电平。 要操作的管脚在参数1 中指定 1 参数1 1 byte 参数值范围为
     *            0x01~0x04，分别对应要操作的端口 IO1~IO4 2 参数2 1 byte 参数值为0x00 或0x01。
     *            Parameter0 Parameter2 描述 0x00 0x00 IO 配置为输入模式 0x00 0x01 IO
     *            配置为输出模式 0x01 0x00 设置 IO 输出为低电平 0x01 0x01 设置 IO 输出为高电平 当参数0为
     *            0x02 时，此参数无意义。
     * @param bufout
     * @return
     */
    static public native int SetIOParameter(byte p1, byte p2, byte p3,
                                            byte[] bufout);

    static public int InventoryLables() {
        int nret = Inventory();
        if (!m_bASYC) {
            StartASYClables();
        }
        return nret;
    }

    static public int InventoryLablesLoop() {
        int nret = Inventory();
        m_bLoop = true;
        if (!m_bASYC) {
            StartASYClables();
        }
        return nret;
    }

    static public void StopLoop() {
        m_bLoop = false;
    }

    static public int MultInventoryLables() {
        int nret = MultiInventory(65535);
        if (!m_bASYC) {
            StartASYClables();
        }
        return nret;
    }

    /**
     * 读标签（结果通过Handle异步发送，一张卡一条消息）
     *
     * @param password
     *            读密码，4个字节
     * @param nUL
     *            PC+EPC长度
     * @param PCandEPC
     *            PC+EPC数据
     * @param membank
     *            标签数据存储区
     * @param nSA
     *            读标签数据区地址偏移
     * @param nDL
     *            读标签数据区地址长度
     * @return
     */
    static public int ReadLables(byte[] password, int nUL, byte[] PCandEPC,
                                 byte membank, int nSA, int nDL) {
        int nret = 0;
        if (!m_bASYC) {
            Clean();
            nret = ReadLable(password, nUL, PCandEPC, membank, nSA, nDL);
            m_bOK = false;
            m_nReSend = 0;
            StartASYCReadlables();
            while ((!m_bOK) && (m_nReSend < 20)) {
                m_nReSend++;
                ReadLable(password, nUL, PCandEPC, membank, nSA, nDL);
            }
        }
        return nret;
    }

    /**
     * 对单个标签，锁定 Lock 或者解锁 Unlock 该标签的数据存储区
     *
     * @param password
     *            锁定密码
     * @param nUL
     *            PC+EPC长度
     * @param PCandEPC
     *            PC+EPC数据
     * @param nLD
     *            锁定还是解锁命令
     * @return
     */
    static public int LockLables(byte[] password, int nUL, byte[] PCandEPC,
                                 int nLD) {
        Clean();
        int nret = Lock(password, nUL, PCandEPC, nLD);
        if (!m_bASYC) {
            StartASYCLocklables();
        }
        return nret;
    }

    // static public int Selectlables()
    // /**
    // * 锁定标签 （结果通过Handle异步发送）
    // * @param btReadId 读写器地址
    // * @param pbtAryPassWord 访问密码（4个字节）
    // * @param btMembank 锁定区域（访问密码（4）、销毁密码（5）、EPC（3）、TID（2）、USER（1））
    // * @param btLockType 锁定类型（开放（0）、锁定（1）、永久开放（2）、永久锁定（3））
    // * @return
    // */
    // static public int LockLables(byte btReadId, byte[] pbtAryPassWord,
    // byte btMembank, byte btLockType) {
    // Clean();
    // int nret = LockTag(btReadId, pbtAryPassWord, btMembank, btLockType);
    // if (!m_bASYC) {
    // StartASYCLocklables();
    // }
    // return nret;
    // }

    /**
     * 写标签（结果通过Handle异步发送，一张卡一条消息）
     *
     * @param password
     *            密码4字节
     * @param nUL
     *            PC+EPC长度
     * @param PCandEPC
     *            PC+EPC数据
     * @param membank
     *            标签数据存储区
     * @param nSA
     *            写标签数据区地址偏移
     * @param nDL
     *            写标签数据区数据长度
     * @param data
     *            写入数据
     * @return
     */
    static public int Writelables(byte[] password, int nUL, byte[] PCandEPC,
                                  byte membank, int nSA, int nDL, byte[] data) {
        Clean();
        int nret = WriteLable(password, nUL, PCandEPC, membank, nSA, nDL, data);
        if (!m_bASYC) {
            m_bOK = false;
            m_nReSend = 0;
            StartASYCWritelables();
            while ((!m_bOK) && (m_nReSend < 20)) {
                m_nReSend++;
                WriteLable(password, nUL, PCandEPC, membank, nSA, nDL, data);
            }
        }
        return nret;
    }

    static public int GetLockPayLoad(byte membank, byte Mask) {
        int nret = 0;
        switch (Mask) {
            case 0:
                switch (membank) {
                    case 0:
                        nret = 0x80000;
                        break;
                    case 1:
                        nret = 0x80200;
                        break;
                    case 2:
                        nret = 0xc0100;
                        break;
                    case 3:
                        nret = 0xc0300;
                        break;
                }
                break;
            case 1:
                switch (membank) {
                    case 0:
                        nret = 0x20000;
                        break;
                    case 1:
                        nret = 0x20080;
                        break;
                    case 2:
                        nret = 0x30040;
                        break;
                    case 3:
                        nret = 0x300c0;
                        break;
                }
                break;
            case 2:
                switch (membank) {
                    case 0:
                        nret = 0x8000;
                        break;
                    case 1:
                        nret = 0x8020;
                        break;
                    case 2:
                        nret = 0xc010;
                        break;
                    case 3:
                        nret = 0xc030;
                        break;
                }
                break;
            case 3:
                switch (membank) {
                    case 0:
                        nret = 0x2000;
                        break;
                    case 1:
                        nret = 0x2008;
                        break;
                    case 2:
                        nret = 0x3004;
                        break;
                    case 3:
                        nret = 0x300c;
                        break;
                }
                break;
            case 4:
                switch (membank) {
                    case 0:
                        nret = 0x0800;
                        break;
                    case 1:
                        nret = 0x0802;
                        break;
                    case 2:
                        nret = 0x0c01;
                        break;
                    case 3:
                        nret = 0x0c03;
                        break;
                }
                break;
        }
        return nret;
    }

    static void StartASYCKilllables() {
        m_bASYC = true;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                int nTemp = 0;
                m_nCount = 0;
                while (m_handler != null) {

                    nTemp = Read(m_buf, m_nCount, 1024);
                    m_nCount += nTemp;
                    if (nTemp == 0)
                        break;
                    String str = reader.BytesToString(m_buf, 0, m_nCount);
                    String[] substr = Pattern.compile("BB0165").split(str);
                    for (int i = 0; i < substr.length; i++) {
                        if (substr[i].length() >= 10) {
                            if (substr[i].substring(0, 10).equals("000100677E")) {
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = "OK";
                                m_handler.sendMessage(msg);
                            }
                        }

                    }

                }

                m_bASYC = false;
            }
        });
        thread.start();
    }

    static void StartASYCLocklables() {
        m_bASYC = true;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                int nTemp = 0;
                m_nCount = 0;
                while (m_handler != null) {

                    nTemp = Read(m_buf, m_nCount, 1024);
                    m_nCount += nTemp;
                    if (nTemp == 0)
                        break;
                    String str = reader.BytesToString(m_buf, 0, m_nCount);
                    String[] substr = Pattern.compile("BB0182").split(str);
                    for (int i = 0; i < substr.length; i++) {
                        if (substr[i].length() >= 10) {
                            if (substr[i].substring(0, 10).equals("000100847E")) {
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = "OK";
                                m_handler.sendMessage(msg);
                            }
                        }

                    }

                }

                m_bASYC = false;
            }
        });
        thread.start();
    }

    static void StartASYCWritelables() {
        m_bASYC = true;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                int nTemp = 0;
                m_nCount = 0;
                m_nread = 0;
                while (m_handler != null) {

                    nTemp = Read(m_buf, m_nCount, 1024);
                    m_nCount += nTemp;
                    if (nTemp == 0) {
                        m_nread++;
                        if (m_nread > 5)
                            break;
                    }
                    String str = reader.BytesToString(m_buf, 0, m_nCount);
                    Log.e("write", str);
                    String[] substr = Pattern.compile("BB0149").split(str);
                    for (int i = 0; i < substr.length; i++) {
                        if (substr[i].length() >= 10) {
                            if (substr[i].substring(0, 10).equals("0001004B7E")) {
                                m_bOK = true;
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = "OK";
                                m_handler.sendMessage(msg);
                            }
                        }

                    }

                }

                m_bASYC = false;
            }
        });
        thread.start();
    }

    static void StartASYCReadlables() {
        m_bASYC = true;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                int nTemp = 0;
                m_nCount = 0;
                m_nread = 0;
                while (m_handler != null) {

                    nTemp = Read(m_buf, m_nCount, 1024);
                    m_nCount += nTemp;
                    if (nTemp == 0) {
                        m_nread++;
                        if (m_nread > 5)
                            break;
                    }
                    String str = reader.BytesToString(m_buf, 0, m_nCount);
                    Log.e("1111111", str);
                    String[] substr = Pattern.compile("BB0139").split(str);
                    for (int i = 0; i < substr.length; i++) {
                        // Log.e("222222",substr[i]);
                        if (substr[i].length() > 10) {
                            if (!substr[i].substring(0, 2).equals("BB")) {
                                m_bOK = true;
                                Message msg = new Message();
                                msg.what = (substr[i].length() - 8) / 2;
                                msg.obj = substr[i].substring(4,
                                        substr[i].length() - 4);
                                m_handler.sendMessage(msg);
                            }
                        }
                    }

                }

                m_bASYC = false;
            }
        });
        thread.start();
    }

    static void StartASYClables() {
        m_bASYC = true;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                int nTemp = 0, nIndex = 0;
                boolean tag_find = false;
                m_nCount = 0;
                m_nReSend = 0;
                nIndex=0;
                while (m_handler != null) {
                    //nIndex = m_nCount;
                    nTemp = Read(m_buf, m_nCount, 10240 - m_nCount);
                    m_nCount += nTemp;
                    Log.e("777777777777777777", "count=" + m_nCount);
                    if (nTemp == 0) {

                        String str = reader.BytesToString(m_buf, nIndex,
                                m_nCount - nIndex);
                        Log.e("77777777777777", str);
                        String[] substr = Pattern.compile("BB0222").split(str);
                        // Log.e("9999999", "len=" + substr.length);
                        for (int i = 0; i < substr.length; i++) {
                            Log.e("777777", substr[i]);
                            if (substr[i].length() > 16) {
                                if (!substr[i].substring(0, 2).equals("BB")) {
                                    int nlen = Integer.valueOf(
                                            substr[i].substring(0, 4), 16);
                                    // Log.e("777777",substr[i].substring(0,4));
                                    if ((nlen > 3)
                                            && (nlen < (substr[i].length() - 6) / 2)) {
                                        Message msg = new Message();
                                        //	m_handler.removeMessages(0);
                                        msg.what = (substr[i].length() - 12) / 2;
                                        msg.obj = substr[i].substring(6,
                                                nlen * 2);
                                        m_handler.sendMessage(msg);
                                        tag_find = true;
                                        m_bOK = true;
                                    }
                                }
                            }
                        }
                        if (tag_find) {

                            mSoundPool.play(msound, 1.0f, 1.0f, 0, 0, 1.0f);
                        }
                        if (m_bLoop) {
                            m_nCount = 0;
                            InventoryLablesLoop();
                            tag_find = false;
                        } else {
                            if ((m_nReSend < 20) && (!tag_find)) {
                                Inventory();
                                m_nReSend++;
                            } else
                                break;
                            tag_find = false;
                            Log.e("efsfsd", "m_nReSend=" + m_nReSend);
                        }

                        if (m_nCount >= 1024)
                            m_nCount = 0;
                    }
                }
                // Log.e("end", "quit");
                m_bASYC = false;
            }
        });
        thread.start();

    }


    static {
        System.loadLibrary("uhf-tools");
        msound = mSoundPool.load("/system/media/audio/notifications/Argon.ogg", 1);
    }

    public static byte[] stringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String BytesToString(byte[] b, int nS, int ncount) {
        String ret = "";
        int nMax = ncount > (b.length - nS) ? b.length - nS : ncount;
        for (int i = 0; i < nMax; i++) {
            String hex = Integer.toHexString(b[i + nS] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    public static int byteToInt(byte[] b) // byteToInt
    {
        int t2 = 0, temp = 0;
        for (int i = 3; i >= 0; i--) {
            t2 = t2 << 8;
            temp = b[i];
            if (temp < 0) {
                temp += 256;
            }
            t2 = t2 + temp;

        }
        return t2;

    }

    public static int byteToInt(byte[] b, int nIndex, int ncount) // byteToInt
    {
        int t2 = 0, temp = 0;
        for (int i = 0; i < ncount; i++) {
            t2 = t2 << 8;
            temp = b[i + nIndex];
            if (temp < 0) {
                temp += 256;
            }
            t2 = t2 + temp;

        }
        return t2;

    }

    /**** int to byte ******/
    public static byte[] intToByte(int content, int offset) {

        byte result[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        for (int j = offset; j < result.length; j += 4) {
            result[j + 3] = (byte) (content & 0xff);
            result[j + 2] = (byte) ((content >> 8) & 0xff);
            result[j + 1] = (byte) ((content >> 16) & 0xff);
            result[j] = (byte) ((content >> 24) & 0xff);
        }
        return result;
    }

}

