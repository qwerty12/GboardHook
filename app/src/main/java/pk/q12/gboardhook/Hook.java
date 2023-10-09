package pk.q12.gboardhook;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.File;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public final class Hook implements IXposedHookLoadPackage, IXposedHookInitPackageResources {
    private static final String TARGET_APP = "com.google.android.inputmethod.latin";
    private static final String FLAGS_BASENAME = "flag_override";

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (lpparam == null || !lpparam.isFirstApplication || lpparam.classLoader == null || lpparam.packageName == null || !lpparam.packageName.equals(TARGET_APP))
            return;

        if ((new File("/data/data/" + TARGET_APP + "/shared_prefs/" + FLAGS_BASENAME + ".xml")).exists())
            return;

        final XC_MethodHook.Unhook[] init = new XC_MethodHook.Unhook[1];
        init[0] = findAndHookMethod("com.google.android.apps.inputmethod.libs.framework.core.LauncherActivity", lpparam.classLoader, "onCreate", android.os.Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                init[0].unhook();
                final Activity activity = (Activity) param.thisObject;

                final Context context = activity.getApplicationContext();
                SharedPreferences sharedPreferences = context.getSharedPreferences(FLAGS_BASENAME, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("enable_tablet_large", false);
                editor.putBoolean("enable_sharing", false);
                editor.putBoolean("enable_email_provider_completion", true);
                editor.putBoolean("enable_secondary_symbols", true);
                editor.putBoolean("show_language_switch_key", false);
                editor.putBoolean("show_suggestions", true);
                editor.putBoolean("show_branding_on_space", false);
                editor.putLong("show_branding_interval_seconds", 0);
                editor.putLong("branding_fadeout_delay_ms", 0);
                editor.putBoolean("enable_backup_clipboard_data", false);
                editor.putBoolean("config_voice_typing", false);
                editor.putBoolean("enable_drag_inside_access_points_panel", true);
                editor.putString("access_points_order", "clipboard;settings;one_handed;textediting;split;floating_keyboard;keyboard_resizing");
                editor.putString("config_access_points_order", "clipboard;settings;one_handed;textediting;split;floating_keyboard;keyboard_resizing");
                editor.putString("apps_to_respect_no_auto_correction", "com.google.android.apps.nexuslauncher,ginlemon.flowerfree,com.teslacoilsw.launcher");
                editor.putBoolean("enable_fallback_ondevice_recognizer", false);
                editor.putBoolean("hide_offline_speech_recognition", true);
                editor.putBoolean("enable_downloadable_spell_checker_model", true);

                for (int i = 0; i < 3; ++i) {
                    // Bow before my coding prowess.
                    editor.commit();
                    Thread.sleep(100);
                }

                Toast.makeText(context, "Wait a second and relaunch Gboard to complete the setup process.", Toast.LENGTH_SHORT).show();
                activity.finishAndRemoveTask();
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        if (resparam != null && resparam.packageName != null && resparam.packageName.equals(TARGET_APP)) {
            resparam.res.setReplacement(TARGET_APP, "string", "firebase_database_url", "https://sfnjhsfdjinksdfo.xcvdf");
        }
    }
}
