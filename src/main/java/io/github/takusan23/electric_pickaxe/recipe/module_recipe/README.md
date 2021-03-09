ここをメンテしに来た私に

一部のモジュールアイテムは、クラフト時の材料がNBTに依存しています。  

例：シルクタッチ付きの道具＋幸運付きの道具＋モジュール

そのため、JSONによるレシピ記述ができません。ModuleRecipe.java参照

なので、ModuleRecipeクラスから使うために、  
アイテムが作成可能かどうかのメソッドや、完成品を返すメソッドがそれぞれ書いてあるクラスがあるのがここ。

あとJEIにレシピ情報を渡すために使う必要な材料を返すメソッドもここにあるクラスに書いてある。

エンチャントやらポーション効果やらでそれぞれ条件が違うのでややこしい。

# ModuleRecipeInterface
こいつを継承するとなんのメソッドを実装すべきかわかります。

- match(List<ItemStack> itemList)
    - これは引数のアイテム配列でクラフトが可能かどうかを判断するメソッドになってます。
    
使用例：
```java
public class ModuleRecipe extends SpecialRecipe {

    public static final SilkTouchFortuneModuleRecipe SILK_TOUCH_FORTUNE_MODULE_RECIPE = new SilkTouchFortuneModuleRecipe();

    public ModuleRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        // 作業台に乗ってるアイテム配列
        ArrayList<ItemStack> craftingItemList = new ArrayList<>();
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inv.getStackInSlot(j);
            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() != Items.AIR) {
                    craftingItemList.add(itemstack1);
                }
            }
        }
        
        // シルクタッチ、幸運モジュールがある場合
        if (SILK_TOUCH_FORTUNE_MODULE_RECIPE.match(craftingItemList)) {
            return true;
        }
        
        return false;
    }
}
```

- getResultItem()
    - 完成品を返すメソッドです。そのまま
    
使用例：

```java
public class ModuleRecipe extends SpecialRecipe {

    public static final SilkTouchFortuneModuleRecipe SILK_TOUCH_FORTUNE_MODULE_RECIPE = new SilkTouchFortuneModuleRecipe();

    public ModuleRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {

        // モジュール以外のアイテム
        ArrayList<ItemStack> craftingItemList = new ArrayList<>();
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inv.getStackInSlot(j);
            if (itemstack1.getItem() != Items.AIR) {
                craftingItemList.add(itemstack1);
            }
        }

        // シルクタッチ、幸運モジュールがある場合
        if (SILK_TOUCH_FORTUNE_MODULE_RECIPE.match(craftingItemList)) {
            return SILK_TOUCH_FORTUNE_MODULE_RECIPE.getResultItem();
        }

        return ItemStack.EMPTY;
    }
}
```

- getNeedItemList()
    - 材料を返してあげてください。JEIへ登録する際に使います。
    
使用例：
```java
/**
 * JEIへ独自レシピ（{@link ModuleRecipe}）を教えるためのクラス
 */
@JeiPlugin
public class JEIPlugin implements IModPlugin {
  /**
   * レシピを教えるときに使う
   */
  ResourceLocation location = new ResourceLocation(ElectricPickaxe.MOD_ID, "module_recipe");

  /**
   * わからん
   */
  @Override
  public ResourceLocation getPluginUid() {
    return new ResourceLocation(ElectricPickaxe.MOD_ID, "jei_plugin");
  }

  /**
   * JEIに表示させるレシピをここで登録する
   */
  @Override
  public void registerRecipes(IRecipeRegistration registration) {

    // レシピ配列
    ArrayList<IRecipe> recipeArrayList = new ArrayList<>();

    recipeArrayList.add(new ShapelessRecipe(location,
            "",
            ModuleRecipe.SILK_TOUCH_FORTUNE_MODULE_RECIPE.resultItem(),
            itemStackListToNonNullList(ModuleRecipe.SILK_TOUCH_FORTUNE_MODULE_RECIPE.getNeedItemList())
    ));

    registration.addRecipes(recipeArrayList, VanillaRecipeCategoryUid.CRAFTING);
  }

  /**
   * ItemStack配列をNonNullListに変換する
   * <p>
   * Kotlinの拡張関数ほしい
   */
  private NonNullList<Ingredient> itemStackListToNonNullList(List<ItemStack> itemStackList) {
    NonNullList<Ingredient> itemStackNonNullList = NonNullList.create();
    itemStackList.forEach(itemStack -> itemStackNonNullList.add(Ingredient.fromStacks(itemStack)));
    return itemStackNonNullList;
  }

}
```