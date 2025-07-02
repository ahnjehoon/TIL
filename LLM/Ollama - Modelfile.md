모델 설정과 동작 방식을 정의하는 파일

## 기본 구조
- FROM: 사용할 모델 파일 경로 지정
- TEMPLATE: 프롬프트 템플릿 형식
- PARAMETER: 모델 파라미터 설정
- SYSTEM: 시스템 역할 및 지시사항

### PARAMTER 설정
- temperature: 창의성 조절 (0-1, 높을수록 창의적)
- top_p: 응답 다양성 제어 (상위 확률 기준)
- num_ctx: 컨텍스트 윈도우 크기 (기본값 2048)
- num_predict: 생성 토큰 수 (기본값 128)
- stop: 생성 중단 키워드

## 샘플
```
# Modelfile 내용
FROM "./model.gguf"

TEMPLATE """<|im_start|>system
{{.System}}<|im_end|>
<|im_start|>user
{{.Prompt}}<|im_end|>
<|im_start|>assistant
"""

PARAMETER temperature 0.7
PARAMETER top_p 0.9
PARAMETER num_ctx 4096
PARAMETER stop "<|im_start|>"
PARAMETER stop "<|im_end|>"
PARAMETER stop "<|end_of_text|>"

SYSTEM "당신은 한국어로 대화하는 도움이 되는 AI 어시스턴트입니다."
```

## 모델 생성 및 실행

```
# 모델 생성
ollama create custom-model -f Modelfile

# 모델 목록 확인 
ollama list 

# 모델 실행 
ollama run custom-model

# 기존 모델 삭제 (필요시)
ollama rm custom-model
```


### 주의사항
- 아래 항목 추가 안하면 지혼자 막 떠듬
```
PARAMETER stop "<|im_start|>"
PARAMETER stop "<|im_end|>"
PARAMETER stop "<|end_of_text|>"

```
