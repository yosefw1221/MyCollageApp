//package com.yotor.solution.mytimetable;
//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Handler;
//import android.telephony.TelephonyManager;
//
////import com.crashlytics.android.Crashlytics;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.InterstitialAd;
//import com.google.android.gms.ads.NativeExpressAdView;
//import com.google.android.gms.ads.VideoOptions;
//
//
//public class AdsUtil {
//    public static int ad_update_interval_length = 30000;
//    private static boolean stopHandler = false;
//    public static InterstitialAd ad;
//    public static String[] AdsError = {"ERROR_INTERNAL_ERROR", "ERROR_INVALID_REQUEST", "ERROR_NETWORK_ERROR", "ERROR_NO_FILL"};
//    private static int ad_count = 0;
//    private static NativeExpressAdView adView;
//
//    public static int getNetworkType(Context context) {
//        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        if (manager == null) return -1;
//        return manager.getNetworkType();
//    }
//
//    public static boolean isNetworkConnected(Context context) {
//        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (manager == null) return false;
//        NetworkInfo info = manager.getActiveNetworkInfo();
//        return (info != null) && info.isConnected();
//
//    }
//
//
//    public static void loadInterstitialAd(final Context context) {
//        try {
//            if (!isNetworkConnected(context)) {
//                ad = null;
//               //Analytics.logAppEvent("ADS ", "NETWORK DISCONNECTED");
//                return;
//            }
//            if (ad != null)
//                if (ad.isLoaded() || ad.isLoading()){
//                    return;
//                }
//
//            ad = null;
//
//            ad = new InterstitialAd(context);
//            ad.setAdUnitId("ca-app-pub-2301026347150212/7069826722");
//            ad.loadAd(new AdRequest.Builder().build());
//            ad.setAdListener(new AdListener() {
//                @Override
//                public void onAdLoaded() {
//                    super.onAdLoaded();
//                   //Crashlytics.log(1, "ADS_LOADED", " count " + ad_count);
//                   //Analytics.logAds("ADS_LOADED");
//                }
//
//                @Override
//                public void onAdFailedToLoad(int i) {
//                    super.onAdFailedToLoad(i);
//                   //Crashlytics.log(1, "ADS_NOT_LOAD", "ERROR_CODE " + i);
//                    if (i < AdsError.length)//Analytics.logAds(AdsError[i]);
//
//                }
//
//                @Override
//                public void onAdClosed() {
//                    super.onAdClosed();
//                    showInterstitialAd(context, AppConfig.getAdInterval());
//                   //Analytics.logAds("ADS_CLOSED");
//
//                }
//
//                @Override
//                public void onAdClicked() {
//                    super.onAdClicked();
//                   //Analytics.logAds("ADS_CLICKED");
//
//                }
//
//                @Override
//                public void onAdLeftApplication() {
//                    super.onAdLeftApplication();
//
//                }
//            });
//        } catch (Exception e) {
//           //Crashlytics.logException(e);
//        }
//    }
//
//    static void showInterstitialAd(final Context context, final int interval) {
//        try {
//            loadInterstitialAd(context);
//            Handler handler = new Handler();
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//
//                        if (ad != null) {
//                            if (ad.isLoaded()) {
//                                ad.show();
//                                ad_count++;
//                               //Analytics.logAds("ADS_DISPLAYED_" + ad_count);
//                            }
//                        }
//                    } catch (Exception e) {
//                       //Crashlytics.logException(e);
//                    }
//                }
//            };
//                handler.postDelayed(runnable, (interval * 1000) + ad_count * AppConfig.getAdUpdate());
//        } catch (Exception e) {
//           //Crashlytics.logException(e);
//        }
//    }
//
//    public static void loadNativeAds(final Context context,  int width, final adsCallback callback) {
//        if (!isNetworkConnected(context))return;
//        if (adView!=null)
//        if (adView.isLoading()) return;
//
//        adView = new NativeExpressAdView(context);
//        adView.setAdUnitId("ca-app-pub-2301026347150212/5733721995");
//        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
//        adView.setVideoOptions(new VideoOptions.Builder().setStartMuted(true).build());
//        adView.loadAd(new AdRequest.Builder().build());
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                callback.onLoaded(adView);
//            }
//            @Override
//            public void onAdFailedToLoad(int i) {
//                super.onAdFailedToLoad(i);
//                callback.onField(i);
//            }
//        });
//
//    }
//
//    public interface adsCallback {
//        void onLoaded(NativeExpressAdView expressAdView);
//        void onField(int errorCode);
//
//    }
//}
