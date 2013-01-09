// SendInputConsole.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "EventSender.h"
#include <Windows.h>


int _tmain(int argc, _TCHAR* argv[])
{
	for(int i=0;i<10;i++){
		INPUT input[1];
		memset(input, 0, sizeof(input));
		input[0].type=INPUT_MOUSE;
		input[0].mi.dwFlags = MOUSEEVENTF_MOVE;
		input[0].mi.dx = 10;
		input[0].mi.dy = 10;
		input[0].mi.mouseData = 0;
		SendInput(1, input, sizeof(INPUT));
	}

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

	getchar();

	return 0;
}

