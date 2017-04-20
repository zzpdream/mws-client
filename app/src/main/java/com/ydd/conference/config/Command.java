package com.ydd.conference.config;

/**
 * Created by hehelt on 16/2/26.
 */

import java.util.IllegalFormatCodePointException;

/**
 * 代表主机和代表终端通讯的命令字定义
 * <p/>
 * Created by ranfi on 2/22/16.
 */
public enum Command {

    INITIALIZE("initialize", "终端初始化", 1),

    HEARTBEAT("heartbeat", "心跳包", 3),

    SHUTDOWN("shutdown", "关闭终端", 2),

    REBOOT("reboot", "重启终端", 4),

    START_PLAY_VIDEO("startPlayVideo", "开始播放视频流", 5),

    STOP_PLAY_VIDEO("stopPlayVideo", "停止播放视频流", 6),

//    DOWNLOAD_PHOTO("downloadPhoto", "下载人员照片", 10),

    CLEAR_SEAT("clearSeat", "清除座位", 20),

    SET_SEAT("setSeat", "排座", 21),

    UPDATE_SEAT("updateSeat", "更新座位信息", 22),

    SET_CHAIRMAN("setChairman", "设置主席位置", 23),

    SHOW_LOGO("showLogo", "显示会标", 30),

    SHOW_SUBJECT("showSubject", "显示议程", 31),

    START_REGISTER("startRegister", "开始报到", 40),

    SHOW_REGISTER("showRegister", "显示报到", 41),

    STOP_REGISTER("stopRegister", "结束报到", 42),

    REGISTER("register", "手动报到", 43),

    UNREGISTER("unRegister", "手动销到", 44),

    UPDATE_REGISTER("updateRegister", "更新报到状态", 45),

    START_VOTE("startVote", "申请表决", 50),

    STOP_VOTE("stopVote", "停止表决", 51),

    VOTE("vote", "上传表决", 52),

    START_SPEAK("startSpeak", "开始申请发言", 60),

    REQUEST_SPEAK("requestSpeak", "已申请发言", 61),


    SET_SPEAKING("setSpeaking", "设置正在发言人", 62),

    CANCEL_SPEAKING("cancelSpeaking", "取消和结束正在发言", 63);




    public String value; //命令字
    public String name; //命令名称
    public int status;  //当前命令的业务状态,便于断线后的同步

    Command(String value, String name, int status) {
        this.value = value;
        this.name = name;
        this.status = status;
    }

    public static Command getCommand(String cmd) {
        for (Command command : Command.values()) {
            if (command.value.equalsIgnoreCase(cmd)) {
                return command;
            }
        }
        return null;
        //throw new IllegalFormatCodePointException(1);
    }


}
