- 글 작성 기준 1.0.62가 최신 버전
- SuperClaude를 설치했는데 /sc를 쳐도 아무것도 안나옴

## 문제 원인
- 62버전의 버그같음

## 해결방법
- 1.0.61로 다운그레이드 (현 1.0.62 최신)
```cmd
npm uninstall @anthropic-ai/claude-code
npm install @anthropic-ai/claude-code@1.0.61 -g
claude config set -g autoUpdaterStatus disabled
```

