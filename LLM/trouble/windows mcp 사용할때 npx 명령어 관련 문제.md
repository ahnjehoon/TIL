## 요약
- args에 npx를 다음과 같이 수정
```text
# CMD
cmd /c npx

# POWERSHELL
powershell -c npx

# 예시
claude mcp add filesystem npx @modelcontextprotocol/server-filesystem ./
```


```json
{
  "mcpServers": {
    "filesystem": {
      "command": "cmd",
      "args": [
        "/c", 
        "npx", 
        "@modelcontextprotocol/server-filesystem", 
      ]
    }
  }
}
```

## 명령어 실행 방식 차이

```text
[Warning] [filesystem] mcpServers.filesystem: Windows requires 'cmd /c' wrapper to execute npx
```

- **문제**: `npx` 명령어를 직접 실행할 수 없음
    
- **원인**: Windows CMD/PowerShell에서는 Node.js 실행 파일에 대한 래퍼가 필요
    
- **해결**: `cmd /c npx` 또는 `powershell -c npx` 래퍼 사용 필요

### claude code에 mcp 추가 예시
```bash
# 잘못된 방법 (Linux/Mac 방식)
claude mcp add filesystem npx @modelcontextprotocol/server-filesystem ./

# 올바른 방법 (Windows)
claude mcp add filesystem -- cmd /c npx @modelcontextprotocol/server-filesystem ./
```

## 경로 표기법 문제
Windows의 파일 경로 체계가 다른 운영체제와 달라 문제가 발생함

- **백슬래시(`\`) vs 슬래시(`/`) 문제**
    
- **드라이브 문자** (C:, D: 등) 처리
    
- **공백이 포함된 경로** 처리 문제