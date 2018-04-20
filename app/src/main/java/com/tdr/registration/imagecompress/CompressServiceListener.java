package com.tdr.registration.imagecompress;

import java.util.ArrayList;

/**
 * Created by Linus_Xie on 2016/9/21.
 */
public interface CompressServiceListener {
    void onCompressServiceStart();
    void onCompressServiceEnd(ArrayList<LGImgCompressor.CompressResult> compressResults);
}
