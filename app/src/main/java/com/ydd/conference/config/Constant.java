package com.ydd.conference.config;

import com.ydd.conference.entity.StartVoteRequest;
import com.ydd.conference.util.SharedPreferencesUtil;

import java.util.List;

/**
 * Created by hehelt on 16/3/8.
 */
public class Constant {

    public static final int STATUS_UN_REGISTER = 0;
    public static final int STATUS_REGISTER = 1;
    public static final int HAS_VOTE_RIGHT = 1;
    public static final int NO_VOTE_RIGHT = 0;


    public static String textTitle = "";

    public static String voteTitle = "";

//    public static int registerStatus = STATUS_UN_REGISTER;

    public static boolean isManualRegister = false;

    public static boolean isConnected = false;

    public static boolean barIsShow = false;

    public static final String Terminal_FIRST = "1";
    public static final String Terminal_SECOND = "2";

    //192  200   190  191  201 无线到有线
    public static final int KEY_AGREE = 201;//1赞成
    public static final int KEY_AGAINST = 191;//  2反对
    public static final int KEY_MISS = 190;//   3弃权
    public static final int KEY_LEFT = 200;//   3弃权
    public static final int KEY_RIGHT = 192;//   3弃权

    public static final int AGREE = 1;//1赞成
    public static final int AGAINST = 2;//  2反对
    public static final int MISS = 3;//   3弃权

    public static final int STATUS_CARD_NONE = 2;//没有卡
    public static final int STATUS_CARD_ERROR = 3;//卡不对
    public static final int STATUS_CARD_SUCCESS = 1;//正确

    public static boolean hasCard = false;

    public static List<StartVoteRequest.VoteItem> list;


    public static void init() {
//        registerStatus = STATUS_UN_REGISTER;
        SharedPreferencesUtil.setRegisterStatus(STATUS_UN_REGISTER);
        isManualRegister = false;
        hasCard = false;
        isShowDuty = false;
    }

    public static List<String> LOGO_TEXT = null;  //当前会议的会标内容

    public static boolean isSingleVote = false;

    public static boolean isShowDuty = false; //是否显示职位名称


    public static String VIDEO_URL = "http://10.200.8.21/user/LiveVideo2.html";   //视频流媒体的播放地址
    public static String VIDEO_URL2 = "http://10.200.8.22/user/LiveVideo2.html";   //视频流媒体的播放地址

//    public static boolean isScanning = false;

    public static String registerTitle = "";


}
