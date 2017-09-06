//
// VirtualApp Native Project
//

#ifndef NDK_HOOK_NATIVE_H
#define NDK_HOOK_NATIVE_H


#include <jni.h>
#include <dlfcn.h>
#include <stddef.h>
#include <fcntl.h>
#include <sys/system_properties.h>

#include "Helper.h"

enum METHODS {
    OPEN_DEX = 0, CAMERA_SETUP, VIVO_AUDIORECORD_NATIVE_CHECK_PERMISSION, AUDIORECORD_SETUP, MEDIARECORDER_SETUP
};

void patchAndroidVM(jobjectArray javaMethods, jstring packageName, jboolean isArt, jint apiLevel, jintArray methodTypes);

void *getVMHandle();


#endif //NDK_HOOK_NATIVE_H
