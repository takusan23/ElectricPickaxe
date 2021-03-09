package io.github.takusan23.electric_pickaxe.tool;

import net.minecraft.util.text.TranslationTextComponent;

public class LocalizeString {

    /**
     * ja-jpとかのローカライズした文字列を返す
     *
     * AndroidのContext#getString()にあたる
     *
     * @param key 名前。Visualarts
     * @return ローカライズ済みテキスト
     * */
    public static String getLocalizeString(String key){
        return new TranslationTextComponent(key).getString();
    }

}
