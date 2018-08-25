# LaunchTest

ActivityのlaunchModeの違いを確認したい人のためにつくってみました。
このアプリをインストールすると、ランチャーに４つの起動アイコンが表示されます。
それぞれ、Standard（デフォルト）、SingleTop、SingleTask、SingleInstanceで起動するようにしています。

DeepLinkにも対応しており、以下のアドレスで起動するようにしました。
1. https://launchtest.rl/standard
2. https://launchtest.rl/singletask
3. https://launchtest.rl/singletop
4. https://launchtest.rl/singleinstance

画面間の動きも見られるように、トップ画面含めた以下の8画面分への遷移ボタンを用意しました。
1. STANDARD TOP<br>
launchmode="standard"で起動する画面<br>
ランチャーからの起動、およびDeepLink起動も可能
2. SINGLETOP TOP<br>
launchmode="singleTop"で起動する画面<br>
ランチャーからの起動、およびDeepLink起動も可能
3. SINGLETASK TOP<br>
launchmode="singleTask"で起動する画面<br>
ランチャーからの起動、およびDeepLink起動も可能
4. SINGLEINSTANCE TOP<br>
launchmode="singleInstance"で起動する画面<br>
ランチャーからの起動、およびDeepLink起動も可能
5. STANDARD NEXT<br>
launchmode="standard"で起動する画面<br>
TOP画面から次画面遷移テスト用に作成
6. SINGLETOP NEXT<br>
launchmode="singleTop"で起動する画面<br>
TOP画面から次画面遷移テスト用に作成
7. SINGLETASK NEXT<br>
launchmode="singleTask"で起動する画面<br>
TOP画面から次画面遷移テスト用に作成
8. SINGLEINSTANCE NEXT<br>
launchmode="singleInstance"で起動する画面<br>
TOP画面から次画面遷移テスト用に作成

上記画面遷移ボタン押下時にオプションとしてfragスイッチを設定できます。
1. FRAG_ACTIVITY_NEW_TASK<br>
タスクがスタックに存在しても新しいタスクとして起動
2. FRAG_ACTIVITY_SINGLE_TOP<br>
スタック最上位と同じActivityを起動しようとした時に起動しない
3. FRAG_ACTIVITY_CLEAR_TOP<br>
スタックをクリアしてからActivityを起動
4. FRAG_ACTIVITY_NO_HISTORY<br>
スタックに追加せずに起動
5. FRAG_ACTIVITY_LAUNCHED_FROM_HISTORY<br>
スタック内のActivityを利用して起動
6. FRAG_ACTIVITY_EXCLUDE_FROM_RECENTS<br>
履歴内の同一Acrivityを利用して起動
7. FRAG_ACTIVITY_PREVIOUS_IS_TOP<br>
呼び出し元をタスクのトップと見なして起動
8. FRAG_ACTIVITY_REORDER_TO_FRONT<br>
スタック内の同一Activityを最前面に移動

画面表示の際、Logcat上に以下の内容にてFLAG値を参照できるようにしております。<br>
```
LaunchTopActivity: get intent frags : 0x00000000
```
例えば、ランチャーからの起動時、DEEPLINKからの起動時だと値に差がありますが、
そういったことも確認できるかと・・・。

遷移したときのスタック状態は以下のコマンドで確認可能です。<br>
adb shell dumpsys activity | grep "jp.ne.poropi.launchtest" | grep -B 1 "Run #[0-9]*:"
```
      TaskRecord{6d94fb #2082 A=jp.ne.poropi.launchtest U=0 StackId=1 sz=1}
        Run #1: ActivityRecord{7ad67a8 u0 jp.ne.poropi.launchtest/.activity.StandardTop t2082}
--
      TaskRecord{1b3a873 #2075 A=jp.ne.poropi.launchtest U=0 StackId=1 sz=1}
        Run #0: ActivityRecord{3de124d u0 jp.ne.poropi.launchtest/.activity.SingleinstanceNext t2075}
```