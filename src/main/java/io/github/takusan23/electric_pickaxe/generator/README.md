# generator パッケージ
ここではレシピ登録などで使うJSONを人間が書くのはしんどいので、代わりにJavaにやってもらうためのクラスが入ってる。
Javaで記述して、`runData`を実行することでJSONを生成してくれる。

初回実行時は、`build.gradle`の、`minecraft`>`run`>`data`にある`args`のmodIdを自分のに書き換えておいてください。

変更できたら、再度`runIntelljRuns`を実行

あとなんか知らんけど、`Item.Properties()`のところ、最低限クリエイティブタブの設定をしないとぬるぽを吐く模様。

## ローカライズした文字列を取得する方法
ユーティリティクラスを用意しました。

```java
LocalizeString.getLocalizeString("screen.title") // 電動ツルハシの設定
```