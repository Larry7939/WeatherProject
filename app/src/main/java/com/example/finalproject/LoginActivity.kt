package com.example.finalproject

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.kakao.sdk.common.util.Utility
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler

class LoginActivity : AppCompatActivity() {
    lateinit var naverLoginModule: OAuthLogin
    lateinit var mContext: Context
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this
        //네이버 로그인
        binding.loginNaver.setOnClickListener {
            naverLoginModule = OAuthLogin.getInstance()
            naverLoginModule.init(mContext, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.naver_client_name))
            naverLoginModule.startOauthLoginActivity(this, naverLoginhandler)
        }

        //카카오 로그인
        binding.loginKakao.setOnClickListener {
            KakaoSdk.init(this, getString(R.string.kakao_app_key))
            var keyHash = Utility.getKeyHash(this)
            Log.d("KEY_HASH", keyHash)
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)

                } else if (token != null) {
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")
                    val intent = Intent(mContext, LoadingActivity::class.java)
                    startActivity(intent)
                }
            }
            if (LoginClient.instance.isKakaoTalkLoginAvailable(mContext)) {
                LoginClient.instance.loginWithKakaoTalk(mContext, callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(mContext, callback = callback)
            }
        }
    }

        var naverLoginhandler: OAuthLoginHandler = object:OAuthLoginHandler() {
                override fun run(success: Boolean) {
                    if (success) {
                            val intent = Intent(mContext, LoadingActivity::class.java)
                            startActivity(intent)

                    } else {
                        val errorCode: String = naverLoginModule
                            .getLastErrorCode(mContext).code;
                        val errorDesc: String = naverLoginModule.getLastErrorDesc(mContext);
                        Toast.makeText(
                            mContext, "errorCode:" + errorCode
                                    + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
}


