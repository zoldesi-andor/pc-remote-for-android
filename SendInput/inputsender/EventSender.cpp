#include "stdafx.h"
#include "EventSender.h"
#include <Windows.h>
#include <WinUser.h>

/*
 * Class:     hu_za_pc_remote_desktop_agent_EventSender
 * Method:    sendMouseMove
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_hu_za_pc_1remote_desktop_1agent_EventSender_sendMouseMove
  (JNIEnv * env, jobject obj, jint x, jint y){
	  
		INPUT input[1];
		memset(input, 0, sizeof(input));
		input[0].type=INPUT_MOUSE;
		input[0].mi.dwFlags = MOUSEEVENTF_MOVE;
		input[0].mi.dx = (int)x;
		input[0].mi.dy = (int)y;
		input[0].mi.mouseData = 0;

		SendInput(1, input, sizeof(INPUT));
};

/*
 * Class:     hu_za_pc_remote_desktop_agent_EventSender
 * Method:    sendMouseClick
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_hu_za_pc_1remote_desktop_1agent_EventSender_sendMouseClick
  (JNIEnv * env, jobject obj, jint button){
		INPUT input1[1];
		memset(input1, 0, sizeof(input1));
		input1[0].type=INPUT_MOUSE;
		input1[0].mi.dwFlags = MOUSEEVENTF_LEFTDOWN;
		input1[0].mi.dx = 0;
		input1[0].mi.dy = 0;
		input1[0].mi.mouseData = 0;

		SendInput(1, input1, sizeof(INPUT));

		INPUT input2[1];
		memset(input2, 0, sizeof(input1));
		input2[0].type=INPUT_MOUSE;
		input2[0].mi.dwFlags = MOUSEEVENTF_LEFTUP;
		input2[0].mi.dx = 0;
		input2[0].mi.dy = 0;
		input2[0].mi.mouseData = 0;

		SendInput(1, input2, sizeof(INPUT));
};

/*
 * Class:     hu_za_pc_remote_desktop_agent_EventSender
 * Method:    sendKeyPress
 * Signature: (C)V
 */
JNIEXPORT void JNICALL Java_hu_za_pc_1remote_desktop_1agent_EventSender_sendKeyPress
  (JNIEnv * env, jobject obj, jint keyCode){

	  	INPUT input1[1];
		memset(input1, 0, sizeof(input1));
		input1[0].type=INPUT_KEYBOARD;
		input1[0].ki.wVk = keyCode;
		input1[0].ki.dwFlags = 0;
		input1[0].ki.time = 0;
		input1[0].ki.wScan = 0;

		SendInput(1, input1, sizeof(INPUT));

		INPUT input2[1];
		memset(input2, 0, sizeof(input1));
		input2[0].type=INPUT_KEYBOARD;
		input2[0].ki.wVk = keyCode;
		input2[0].ki.dwFlags = KEYEVENTF_KEYUP;
		input2[0].ki.time = 0;
		input2[0].ki.wScan = 0;

		SendInput(1, input2, sizeof(INPUT));
};
