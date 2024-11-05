# Git Workflow Guide

## Daily Development

```bash
# Start new feature
git checkout dev
git pull
git checkout -b feature/your-feature

# Regular commits
git add .
git commit -m "feat(subsystem): add feature description"

# Push changes
git push -u origin feature/your-feature

# Before competition
git checkout main
git pull
git merge dev
git tag -a "competition-v1.0" -m "Competition ready code"
git push origin competition-v1.0
```

## Commit Types

- `feat`: New feature
- `fix`: Bug fix
- `tune`: Parameter tuning
- `test`: Test OpModes
- `docs`: Documentation
- `refactor`: Code restructuring
- `auto`: Autonomous changes

## Emergency Competition Fixes

```bash
# Quick fix during competition
git checkout -b hotfix/issue-description main
# Make changes
git commit -m "fix: critical issue description"
git checkout main
git merge hotfix/issue-description
git push
```

## Code Review Checklist

1. Motor directions verified
2. Constants documented
3. Fail-safes implemented
4. Test OpMode included
5. Hardware map matches
6. Comments clear
7. No hardcoded values
8. Clean architecture