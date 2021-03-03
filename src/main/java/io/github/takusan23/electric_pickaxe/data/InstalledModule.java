package io.github.takusan23.electric_pickaxe.data;

/**
 * インストール済みモジュールのデータクラス
 * <p>
 * Kotlinのデータクラスほしいんだが？
 */
public class InstalledModule {

    private String moduleRegistryId;
    private int level;

    /**
     * @param moduleRegistryId モジュールの登録時の名前
     * @param level            レベル
     */
    public InstalledModule(String moduleRegistryId, int level) {
        this.moduleRegistryId = moduleRegistryId;
        this.level = level;
    }

    /**
     * レベルを返す
     */
    public int getLevel() {
        return level;
    }

    /**
     * 名前を返す。モジュールのアイテムIDが帰ってくると思う
     */
    public String getModuleRegistryId() {
        return moduleRegistryId;
    }

    public void setModuleRegistryId(String moduleRegistryId) {
        this.moduleRegistryId = moduleRegistryId;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
