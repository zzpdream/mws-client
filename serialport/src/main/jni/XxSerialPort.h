/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class android_serialport_api_SerialPort */

#ifndef _Included_com_iznet_xixi_serialport_XxSerialPort
#define _Included_com_iznet_xixi_serialport_XxSerialPort
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     thinkdroid_framework_compoment_serialport_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */
JNIEXPORT jobject JNICALL Java_com_iznet_xixi_serialport_XxSerialPort_open
  (JNIEnv *, jclass, jstring, jint, jint);

/*
 * Class:     thinkdroid_framework_compoment_serialport_SerialPort
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_iznet_xixi_serialport_XxSerialPort_close
  (JNIEnv *, jobject);


JNIEXPORT void JNICALL Java_com_iznet_xixi_serialport_XxSerialPort_getSerialName
(JNIEnv *, jint);

#ifdef __cplusplus
}
#endif
#endif
