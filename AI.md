For AI, following models in Ollama are suitable for programming:

| VRAM | Recommended Model | Parameters | Approx. VRAM | Quantization | Run Command (example) | Notes |
|------|-------------------|------------|--------------|--------------|------------------------|-------|
| 4GB  | Qwen2.5-Coder 1.5B | 1.5B | ~1.5 GB | Q4_K_M | `ollama run qwen2.5-coder:1.5b` | Suitable for code completion, simple explanations, and learning; limited by VRAM, can only run tiny models. |
| 8GB  | Qwen2.5-Coder 7B | 7B | ~5-6 GB | Q4_K_M | `ollama run qwen2.5-coder:7b` | **Entry-level choice for Kotlin development**. Handles code generation, debugging, and explanation well. Good balance of performance and cost. |
| 12GB | Qwen2.5-Coder 7B | 7B | ~5-6 GB | Q4_K_M | `ollama run qwen2.5-coder:7b` | The 7B model runs smoothly on GPU with extra headroom for longer context (KV cache). **Most stable and efficient option**. |
| 16GB | DeepSeek-Coder V2 16B | 16B | ~10-11 GB | Q4_K_M | `ollama run deepseek-coder-v2:16b` | Can run larger code models like Code Llama 13B or DeepSeek-Coder 16B. Better at complex code generation and understanding. |
