# 🔧 Como Configurar a URL de Produção

## 📋 Situações Possíveis

### Situação 1: Você TEM um servidor de produção real
Se você tem um servidor onde a aplicação está rodando em produção (ex: AWS, Azure, servidor próprio), configure a URL real.

### Situação 2: Você NÃO tem servidor (apenas para demonstração/teste)
Se é apenas para demonstração ou teste acadêmico, você pode usar `localhost` mesmo. Vou te mostrar como configurar.

---

## 🚀 Opção 1: Configurar URL Real no GitHub (Se tiver servidor)

### Passo a Passo:

1. **Acesse seu repositório no GitHub**
   - Vá para: `https://github.com/SEU_USUARIO/SEU_REPOSITORIO`

2. **Vá em Settings**
   - Clique em **Settings** (no topo do repositório)

3. **Acesse Secrets**
   - No menu lateral esquerdo, clique em **Secrets and variables**
   - Depois clique em **Actions**

4. **Adicione o Secret**
   - Clique no botão **New repository secret**
   - **Name**: `PROD_ENVIRONMENT_URL`
   - **Secret**: Cole a URL completa da sua aplicação em produção
     - Exemplo: `https://app.exemplo.com`
     - Exemplo: `http://meuservidor.com:8000`
     - Exemplo: `https://app.herokuapp.com`
   - Clique em **Add secret**

5. **Pronto!** 
   - Agora os testes vão usar essa URL automaticamente

---

## 🏠 Opção 2: Usar localhost (Para demonstração/teste)

Se você **não tem um servidor real** e quer apenas demonstrar que os testes funcionam, você pode configurar para usar `localhost` mesmo.

### Passo a Passo:

1. **Acesse seu repositório no GitHub**
   - Vá para: `https://github.com/SEU_USUARIO/SEU_REPOSITORIO`

2. **Vá em Settings → Secrets and variables → Actions**

3. **Adicione o Secret com localhost**
   - Clique em **New repository secret**
   - **Name**: `PROD_ENVIRONMENT_URL`
   - **Secret**: `http://localhost:8000`
   - Clique em **Add secret**

4. **IMPORTANTE**: Para funcionar com localhost, você precisa que a aplicação esteja rodando no GitHub Actions. Vou ajustar o workflow para isso.

---

## ⚙️ Ajuste Automático: Workflow que funciona com ou sem URL

O workflow atual já está preparado para:
- ✅ Funcionar **com** URL configurada (testa produção real)
- ✅ Funcionar **sem** URL configurada (pula os testes com aviso)
- ✅ Funcionar **com localhost** (se você configurar `http://localhost:8000`)

---

## 🎯 Para Demonstração Acadêmica

Se você está fazendo isso para um trabalho/projeto acadêmico e **não tem servidor real**, você tem 2 opções:

### Opção A: Configurar localhost no GitHub
1. Configure `PROD_ENVIRONMENT_URL` = `http://localhost:8000` no GitHub
2. O workflow vai tentar testar em localhost (mas só funciona se a aplicação estiver rodando no CI)

### Opção B: Deixar sem configurar (Recomendado)
1. **Não configure** o `PROD_ENVIRONMENT_URL`
2. Os testes vão ser **pulados automaticamente** com uma mensagem informativa
3. O workflow **não vai falhar** - apenas vai mostrar que os testes foram pulados
4. Isso demonstra que você **implementou** os testes pós-deploy, mesmo que não tenha servidor real

**Para demonstração, a Opção B é melhor** porque:
- ✅ Mostra que você implementou a funcionalidade
- ✅ Não falha o pipeline
- ✅ Deixa claro que precisa de configuração para produção real
- ✅ É mais realista (em produção real você teria a URL configurada)

---

## 📝 Resumo Rápido

| Situação | O que fazer |
|----------|-------------|
| **Tem servidor real** | Configure `PROD_ENVIRONMENT_URL` com a URL real no GitHub |
| **Não tem servidor (demonstração)** | **Não configure nada** - os testes serão pulados automaticamente |
| **Quer testar localhost** | Configure `PROD_ENVIRONMENT_URL` = `http://localhost:8000` |

---

## ✅ Verificação

Depois de configurar (ou não configurar), quando você fizer push para `main`:

1. O workflow vai executar
2. Se tiver `PROD_ENVIRONMENT_URL` configurado → Testa em produção
3. Se **não** tiver configurado → Pula os testes com mensagem informativa
4. O pipeline **não falha** em nenhum dos casos

---

## 🆘 Precisa de Ajuda?

Se ainda tiver dúvidas, me avise qual é sua situação:
- Tem servidor real? → Configure a URL real
- É só para demonstração? → Não configure nada (vai funcionar assim mesmo)

