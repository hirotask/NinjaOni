# NinjaOni

NinjaOniは、従来の鬼ごっこゲームに忍者の要素を加えたMinecraftのミニゲームプラグインです。プレイヤーは忍者チームまたは鬼チームに分かれ、それぞれ専用のアイテムを使用して戦略的なゲームを楽しめます。

## 対応環境
- **Minecraft**: 1.17.1+
- **Java**: 16以上
- **Gradle**: 8.0

## 依存プラグイン
- **ProtocolLib**: プロトコル操作
- **CommandAPI**: コマンド管理

## 設定

### config.yml
```yaml
countdown-time: 3          # カウントダウン時間（秒）
game-time: 200             # ゲーム時間（秒）
warp-block-type-oni: DIAMOND_BLOCK    # 鬼用ワープブロック
warp-block-type-spec: IRON_BLOCK      # 観戦者用ワープブロック
tp-location:
  oni:                     # 鬼チームスポーン地点
    x: 75.0
    y: 4.0
    z: -60.0
  player:                  # 忍者チームスポーン地点
    x: 75.0
    y: 4.0
    z: -60.0
money-amount: 20           # 基本報酬金額
```

## テストサーバーの起動方法

```bash
./gradlew buildAndLaunchServer
```

## ライセンス

[MIT](./LICENSE)

## 貢献

バグ報告や機能提案は、GitHubのIssuesページにお願いします。