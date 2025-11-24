# 📚 Guia de Configuração - GitHub Actions para Múltiplos Ambientes

Este guia explica como configurar os pipelines de CI/CD com GitHub Actions para deploys automatizados em múltiplos ambientes.

## 🏗️ Estrutura do Workflow

O projeto possui um único workflow principal:

**`.github/workflows/ci.yml`** - Pipeline completo com CI/CD para todos os ambientes (development, staging, production)

## 🌍 Ambientes Configurados

### 1. **Staging**
- **Branch**: `staging`
- **Trigger**: Push para `staging` ou workflow_dispatch
- **Características**: Deploy automático com testes adicionais

### 2. **Produção (Production)**
- **Branch**: `main` ou `master`
- **Trigger**: Push para `main`/`master` ou workflow_dispatch
- **Características**: Requer aprovação manual (configurar no GitHub)

## 🔐 Configuração de Secrets

Para que os deploys funcionem, você precisa configurar os seguintes secrets no GitHub:

### Secrets para Staging
1. Acesse: **Settings → Secrets and variables → Actions → New repository secret**
2. Adicione os seguintes secrets:
   - `STAGING_DEPLOY_HOST` - Hostname ou IP do servidor de staging
   - `STAGING_DEPLOY_USER` - Usuário SSH para deploy
   - `STAGING_DEPLOY_KEY` - Chave privada SSH (conteúdo completo da chave)
   - `STAGING_DEPLOY_PATH` - Caminho no servidor (opcional, padrão: `/opt/app/staging`)
   - `STAGING_ENVIRONMENT_URL` - URL do ambiente (opcional, para notificações)

### Secrets para Produção
1. Acesse: **Settings → Secrets and variables → Actions → New repository secret**
2. Adicione os seguintes secrets:
   - `PROD_DEPLOY_HOST` - Hostname ou IP do servidor de produção
   - `PROD_DEPLOY_USER` - Usuário SSH para deploy
   - `PROD_DEPLOY_KEY` - Chave privada SSH (conteúdo completo da chave)
   - `PROD_DEPLOY_PATH` - Caminho no servidor (opcional, padrão: `/opt/app/prod`)
   - `PROD_ENVIRONMENT_URL` - URL do ambiente (opcional, para notificações)

## 🛡️ Configuração de Proteção de Ambientes

Para adicionar proteções de segurança aos ambientes (especialmente produção):

### Passo 1: Configurar Ambientes no GitHub
1. Acesse: **Settings → Environments**
2. Clique em **New environment**
3. Crie os ambientes: `staging`, `production`

### Passo 2: Configurar Proteções para Produção
Para o ambiente `production`, configure:

1. **Required reviewers** (Revisores obrigatórios)
   - Adicione usuários ou equipes que devem aprovar deploys
   - Mínimo de 1 aprovador recomendado

2. **Wait timer** (Timer de espera)
   - Configure um delay antes do deploy (ex: 5 minutos)
   - Permite cancelamento em caso de erro

3. **Deployment branches** (Branches de deploy)
   - Restrinja a apenas `main` ou `master`
   - Ou permita apenas branches específicas

### Passo 3: Configurar Secrets por Ambiente
1. Em cada ambiente, você pode configurar secrets específicos
2. Isso permite usar credenciais diferentes por ambiente
3. Acesse: **Settings → Environments → [Nome do Ambiente] → Environment secrets**

## 🚀 Como Usar

### Deploy Automático por Branch

- **Push para `staging`** → Deploy automático para staging
- **Push para `main`/`master`** → Deploy para produção (requer aprovação se configurado)

### Deploy Manual via Workflow Dispatch

1. Acesse: **Actions → CI/CD Pipeline - Staging e Produção**
2. Clique em **Run workflow**
3. Selecione o ambiente desejado (staging ou production)
4. Clique em **Run workflow**
5. Se for produção e tiver aprovações configuradas, aguarde a aprovação

## 📝 Personalização do Deploy

Os workflows estão configurados com comandos de exemplo para deploy via SSH. Você precisa adaptar os comandos na seção `Deploy para [Ambiente]` de cada job conforme sua infraestrutura:

### Exemplo para Servidor Linux com systemd:
```bash
ssh -i "$DEPLOY_KEY" $DEPLOY_USER@$DEPLOY_HOST "mkdir -p $DEPLOY_PATH"
scp -i "$DEPLOY_KEY" target/*.jar $DEPLOY_USER@$DEPLOY_HOST:$DEPLOY_PATH/
ssh -i "$DEPLOY_KEY" $DEPLOY_USER@$DEPLOY_HOST "systemctl restart app-prod"
```

### Exemplo para Docker:
```bash
docker build -t app:$VERSION .
docker tag app:$VERSION registry.example.com/app:$VERSION
docker push registry.example.com/app:$VERSION
ssh -i "$DEPLOY_KEY" $DEPLOY_USER@$DEPLOY_HOST "docker pull registry.example.com/app:$VERSION && docker-compose up -d"
```

### Exemplo para Cloud (AWS, Azure, GCP):
Use os actions específicos da sua plataforma:
- AWS: `aws-actions/configure-aws-credentials`
- Azure: `azure/login`
- GCP: `google-github-actions/auth`

## ✅ Checklist de Configuração

- [ ] Secrets configurados para staging e produção
- [ ] Ambientes criados no GitHub (Settings → Environments): `staging` e `production`
- [ ] Proteções configuradas para produção (aprovações)
- [ ] Chaves SSH geradas e adicionadas aos secrets
- [ ] Servidores configurados para receber deploys
- [ ] URLs dos ambientes configuradas (se aplicável)
- [ ] Testes de smoke configurados (verificação pós-deploy)
- [ ] Notificações configuradas (Slack, email, etc. - opcional)

## 🔍 Troubleshooting

### Deploy falha com erro de autenticação
- Verifique se os secrets estão configurados corretamente
- Confirme que a chave SSH tem permissões corretas (chmod 600)
- Teste a conexão SSH manualmente

### Aprovação não está sendo solicitada
- Verifique se o ambiente está configurado em Settings → Environments
- Confirme que "Required reviewers" está habilitado
- Verifique se você está usando o ambiente correto no workflow

### Artefatos não encontrados
- Confirme que o job `build-and-test` completou com sucesso
- Verifique se o job de deploy tem `needs: build-and-test`

## 📚 Recursos Adicionais

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [GitHub Environments](https://docs.github.com/en/actions/deployment/targeting-different-environments/using-environments-for-deployment)
- [GitHub Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)

