# generated

ここにあるJSONファイルは、  
`src/main/java/io/github/takusan23/electric_pickaxe/generator/EnLanguageGenerator.java`  
などにより`runData`実行時にJSONファイルを生成するのでこのファイルを直接扱うことはありません。  
また実行時に自動的に読み込まれるので`main`フォルダ内に入れる必要もありません。

なお、一部のレシピに関しては`ModuleRecipe.java`により記述されているため注意してください（NBT関係）

Javaからの取得方法

```java
String localizeText = new TranslationTextComponent("tooltip.max_count").getString();
```