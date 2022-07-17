package com.qnmd.library_base.imageloader

import com.bumptech.glide.request.RequestOptions
import com.qnmd.library_base.R
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


object ImageLoader {

    var AES_KEY = "525202f9149e061d"

    var referer = ""

    val keyMap = hashMapOf(
        "0" to "!",
        "1" to ")",
        "9" to "&",
        "a" to "*",
        "c" to "%",
        "3" to ":",
        "2" to "+",
        "d" to "-",
        "6" to "<"
    )

    val cacheRes: Int = R.drawable.ic_place_holder
    val cacheRes_circle: Int = R.drawable.ic_place_holder
    val cacheRes_portrait: Int = R.drawable.ic_place_holder

    var defaultOptions = RequestOptions()
        .error(cacheRes)
        .placeholder(cacheRes)
        .fallback(cacheRes)


    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"


    /**
     * 解密
     * @param  input 解密的字符串
     * @param  key   解密的key
     * @return byte[]
     */
    fun decryptOrigin(input: ByteArray?, key: String): ByteArray? {
        var output: ByteArray? = null
        try {
            val skey = SecretKeySpec(key.toByteArray(), ALGORITHM)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, skey)
            output = cipher.doFinal(input)
        } catch (e: Exception) {
            println(e.toString())
        }
        return output
    }

}