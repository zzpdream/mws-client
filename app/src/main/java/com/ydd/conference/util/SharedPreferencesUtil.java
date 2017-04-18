package com.ydd.conference.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ydd.conference.config.AppApplication;
import com.ydd.conference.config.Command;
import com.ydd.conference.config.Constant;
import com.ydd.conference.entity.ChairmanRequest;
import com.ydd.conference.entity.SetSeatInfoRequest;


/**
 * Created by hehelt on 15/12/23.
 * <p/>
 * 本地SharePreference存储帮助类
 */
public class SharedPreferencesUtil {


    private static SharedPreferences infoShare;

    private static SharedPreferences pictureShare;

    private static SharedPreferences serverShare;

    private static SharedPreferences totalShare;


    /**
     * 是否是正式终端
     * @return
     */
    public static boolean isTerminalFirst(){
        if (SharedPreferencesUtil.getTerminalType().equals(Constant.Terminal_FIRST)){
            return true;
        }
        return false;
    }


    /**
     * 判断是否有高级会议权限
     *
     * 拥有高级会议权限,必须是正式终端和非匿名访问
     *
     *
     * @return
     */
    public static boolean hasAdvancedMeetingPermission(){
        //非正式终端没有高级会议指令权限
        if(!isTerminalFirst()){
            return false;
        }

        //匿名访问用户没有高级会议指令权限
        if(getIsAnonAccess() == 1){
            return false;
        }
        return true;
    }

    /**
     * 匿名访问权限
     *
     * @return
     */
    public static boolean hasAnonAccessPermission(){
        int isAnonAccess = getIsAnonAccess();
        return isAnonAccess == 1;
    }


    //初始化
    private static void initPictureShare() {
        if (pictureShare == null) {
            pictureShare = AppApplication.mContext.getSharedPreferences("picture", Context.MODE_PRIVATE);
        }
    }

    private static void initTotalNum(){
        if(totalShare==null){
            totalShare = AppApplication.mContext.getSharedPreferences("totalNum",Context.MODE_PRIVATE);
        }
    }

    public static void saveTotal(int num){
        initTotalNum();
        totalShare.edit().putInt("total",num).commit();
    }
    public static int getTotal(){
        initTotalNum();
        return totalShare.getInt("total",0);
    }

    public static void saveMd5(String md5) {
        initPictureShare();
        pictureShare.edit().putString("md5", md5).commit();
    }

    public static String getMd5() {
        initPictureShare();
        return pictureShare.getString("md5", "");
    }


    //初始化
    private static void initInfoShare() {
        if (infoShare == null) {
            infoShare = AppApplication.mContext.getSharedPreferences("info", Context.MODE_PRIVATE);
        }
    }

    private static void initServerShare() {
        if (serverShare == null) {
            serverShare = AppApplication.mContext.getSharedPreferences("server", Context.MODE_PRIVATE);
        }
    }

    /**
     * 初始化座位信息
     *
     * @param seatId
     * @param terminalType
     */
    public static void initSeat(String seatId, String terminalType) {
        initInfoShare();
        infoShare.edit().putString("seatId", seatId).putString("terminalType", terminalType).commit();
    }


    public static void saveSeat(SetSeatInfoRequest.SetSeatInfoParams params) {
        initInfoShare();
        //TODO by ranfi
        String memberName = params.getMemberName();
        if (StringUtils.isNotEmpty(memberName)) {
            memberName = memberName.split("_")[0];
            System.out.println("accept member name:" + memberName);
        }
        infoShare.edit()
                .putString("memberName", memberName)
                .putString("duty", params.getDuty())
                .putString("card1", params.getCard1())
                .putString("memberId", params.getMemberId())
                .putString("card2", params.getCard2())
                .putInt("votingRights", params.getVotingRights())
                .putInt("askForLevel", params.getAskForLeave())
                .putInt("cardNoCheck", params.getCardNoCheck())
                .putInt("isAnonAccess", params.getIsAnonAccess())
                .commit();
    }

    public static int getCardNoCheck() {
        initInfoShare();
        return infoShare.getInt("cardNoCheck", 0);
    }

    /**
     * 获取匿名访问权限
     *
     * @return
     */
    public static int getIsAnonAccess() {
        initInfoShare();
        return infoShare.getInt("isAnonAccess", 0);
    }

    public static int getAskForLevel() {
        initInfoShare();
        System.out.println("askForLevel:" + infoShare.getInt("askForLevel", 0));
        return infoShare.getInt("askForLevel", 0);
    }

    /**
     * 保存设置主席座位参数
     *
     * @param params 设置主席座位参数
     */
    public static void saveChairman(ChairmanRequest.ChairmanParams params) {
        initInfoShare();
        infoShare.edit().putString("chairSeatId", params.getSeatId()).putString("videoUrl", "").commit();
    }

    /**
     * 获取主席座位号
     *
     * @return
     */
    public static String getChairSeatId() {
        initInfoShare();
        return infoShare.getString("chairSeatId", "");
    }

    /**
     * 获取主席座位播放URL
     *
     * @return
     */
    public static String getChairVideoUrl() {
        initInfoShare();
        return infoShare.getString("videoUrl", "");
    }

    public static int getVotingRights() {
        initInfoShare();
        return infoShare.getInt("votingRights", 0);
    }

    public static String getMemberId() {
        initInfoShare();
        return infoShare.getString("memberId", "");
    }

    /**
     * 清楚座位信息
     */
    public static void clearSeat() {
        initInfoShare();
        String terminalType = infoShare.getString("terminalType", "");
        String seatId = infoShare.getString("seatId", "");
        String chairSeatId = infoShare.getString("chairSeatId", "");
        infoShare.edit().clear().commit();
        infoShare.edit()
                .putString("seatId", seatId)
                .putString("terminalType", terminalType)
                .putInt("status", Command.CLEAR_SEAT.status)
                .putString("chairSeatId", chairSeatId)
                .commit();

    }

    /**
     * 清楚座位信息
     */
    public static void initialize() {
        initInfoShare();
        infoShare.edit().clear().commit();

    }

    /**
     * 设置座位hao
     */
    public static void setSeatIdAndType(String seatId, String terminalType) {
        initInfoShare();
        infoShare.edit().putString("seatId", seatId).putString("terminalType", terminalType).commit();

    }

    /**
     * 设置座位hao
     */
//    public static void setTerminalType(String terminalType) {
//        initInfoShare();
//        infoShare.edit().putString("terminalType", terminalType).commit();
//    }

    /**
     * 设置座位hao
     */
    public static void setSeatId(String seatId) {
        initInfoShare();
        infoShare.edit().putString("seatId", seatId).commit();

    }

    public static String getSeatId() {
        initInfoShare();
        return infoShare.getString("seatId", "");
    }

    public static String getMemberName() {
        initInfoShare();
        return infoShare.getString("memberName", "");
    }


    /**
     * 获取职务名称
     *
     * @return
     */
    public static String getDuty() {
        initInfoShare();
        return infoShare.getString("duty", "");
    }

    public static String getTerminalType() {
        initInfoShare();
        return infoShare.getString("terminalType", "1");
    }

    public static String getCardNumber() {
        initInfoShare();
        return infoShare.getString("card1", "");
    }

    public static String getCard2Number() {
        initInfoShare();
        return infoShare.getString("card2", "");
    }

    public static boolean setServerIpAndPort(String ip, int port) {
        initServerShare();
        if (getServerIp().equals(ip) && getServerPort() == port) {
            return false;
        }
        serverShare.edit().putString("serverIp", ip).putInt("serverPort", port).commit();
        return true;
    }

//    public static void setServerPort(int port) {
//        initServerShare();
//        serverShare.edit().putInt("serverPort", port).commit();
//    }

    public static String getServerIp() {
        initServerShare();
        return serverShare.getString("serverIp", "192.168.100.51");
    }

    public static int getServerPort() {
        initServerShare();
        return serverShare.getInt("serverPort", 5678);
    }

    public static int getStatus() {
        initInfoShare();
        return infoShare.getInt("status", Command.INITIALIZE.status);
    }

    public static void setStatus(int status) {
        initInfoShare();
        infoShare.edit().putInt("status", status).commit();
    }

    public static boolean seatIdIsRight(String seatId) {
        return true;
//        return getSeatId().equals(seatId);
    }

    public static void setRegisterStatus(int status) {
        initInfoShare();
        System.out.println("set status:" + status);
        infoShare.edit().putInt("registerStatus", status).commit();
    }


    public static int getRegisterStatus() {
        initInfoShare();
        return infoShare.getInt("registerStatus", Constant.STATUS_UN_REGISTER);
    }

//    public static SetSeatInfoRequest.SetSeatInfoParams getSeatInfo() {
//        initInfoShare();
//        SetSeatInfoRequest.SetSeatInfoParams params = new SetSeatInfoRequest.SetSeatInfoParams();
//        params.setSeatId(infoShare.getString("seatId", ""));
//        params.setMemberId(infoShare.getString("memberId", ""));
//        params.setMemberName(infoShare.getString("memberName", ""));
//        params.setCard2(infoShare.getString("card2", ""));
//        params.setCard1(infoShare.getString("card1", ""));
//        params.setSeatId(infoShare.getString("seatId", ""));
//        params.setVotingRights(infoShare.getInt("votingRights", 0));
//        return params;
//    }

    public static SharedPreferences netConfig;

    public static void setIsNetConfig(boolean isSet) {
        netConfig = AppApplication.mContext.getSharedPreferences("netConfig", Context.MODE_PRIVATE);
        netConfig.edit().putBoolean("flag", isSet).commit();
    }

    public static boolean getNetConfig() {
        netConfig = AppApplication.mContext.getSharedPreferences("netConfig", Context.MODE_PRIVATE);
        return netConfig.getBoolean("flag", false);
    }


    public static void saveIp(String ip) {
        if (null != ip && !"".equals(ip)) {
            initLocalIp();
            localIp.edit().putString("ip", ip).commit();
        }
    }

    public static String getLocalIp() {
        initLocalIp();
        String ip = localIp.getString("ip", "0.0.0.0");
        return ip;
    }

    public static void initLocalIp() {
        if (localIp == null) {
            localIp = AppApplication.mContext.getSharedPreferences("localIp", Context.MODE_PRIVATE);
        }
    }

    public static SharedPreferences localIp;

}
