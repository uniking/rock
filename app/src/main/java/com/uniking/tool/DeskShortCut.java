package com.uniking.tool;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v4.graphics.drawable.IconCompat;
import android.text.NoCopySpan;
import android.text.TextUtils;

import com.uniking.activity.ZhiFuBao;
import com.uniking.receiver.ShortCutReceiver;
import com.uniking.rock.R;

import java.util.List;

/**
 * Created by wzl on 11/29/21.
 */

public class DeskShortCut {
    //创建桌面快捷方式,android8.0以下
    public static void addShortCutCompact(Context context, Class activity, String shortLabel, String packageName) {
        try{
            if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
                Intent shortcutInfoIntent = new Intent(context, activity);
                shortcutInfoIntent.setAction(Intent.ACTION_VIEW); //action必须设置，不然报错

                Drawable drawableicon = getAppIcon(context, packageName);

                ShortcutInfoCompat info = new ShortcutInfoCompat.Builder(context, packageName+"_rock")
                        .setIcon(IconCompat.createWithBitmap(drawableToBitmap(drawableicon)))
                        //.setIcon(IconCompat.createWithResource(context, R.drawable.ic_launcher_foreground))
                        .setShortLabel(shortLabel)
                        .setIntent(shortcutInfoIntent)
                        .build();

                //当添加快捷方式的确认弹框弹出来时，将被回调
                PendingIntent shortcutCallbackIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, ShortCutReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
                ShortcutManagerCompat.requestPinShortcut(context, info, shortcutCallbackIntent.getIntentSender());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void launchAPK3(Context context, String packageName) {
        try{
            Intent intent = getAppOpenIntentByPackageName(context, packageName);
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Intent getAppOpenIntentByPackageName(Context context, String packageName) {
        try{
            String mainAct = null;
            PackageManager pkgMag = context.getPackageManager();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);

            @SuppressLint("WrongConstant") List<ResolveInfo> list = pkgMag.queryIntentActivities(intent,
                    PackageManager.GET_ACTIVITIES);
            for (int i = 0; i < list.size(); i++) {
                ResolveInfo info = list.get(i);
                if (info.activityInfo.packageName.equals(packageName)) {
                    mainAct = info.activityInfo.name;
                    break;
                }
            }
            if (TextUtils.isEmpty(mainAct)) {
                return null;
            }
            intent.setComponent(new ComponentName(packageName, mainAct));
            return intent;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据包名获取App的Icon
     *
     * @param pkgName 包名
     */
    public static Drawable getAppIcon(Context context, String pkgName) {
        try {
            if (null != pkgName) {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo info = pm.getApplicationInfo(pkgName, 0);
                return info.loadIcon(pm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getDrawable(R.drawable.ic_launcher_background);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        try{
            Bitmap bitmap = null;

            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                if(bitmapDrawable.getBitmap() != null) {
                    return bitmapDrawable.getBitmap();
                }
            }

            if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
