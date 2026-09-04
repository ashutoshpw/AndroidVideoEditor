package com.devil.videoeditor;

import org.junit.Test;

import static org.junit.Assert.*;

import video.cutter.mp3.activity.AudioPreviewActivity;

public class ExampleUnitTest {
    @Test
    public void visualizerInitialization_isRejectedForInvalidSessionId() throws Exception {
        assertFalse(AudioPreviewActivity.hasUsableAudioSession(0));
        assertFalse(AudioPreviewActivity.hasUsableAudioSession(-1));
    }
}