#!/usr/bin/env bash

set -euo pipefail

usage() {
  cat <<'EOF'
Usage: scripts/release-tag.sh <version> [--push]

Examples:
  scripts/release-tag.sh 0.3.0
  scripts/release-tag.sh 0.3.0 --push
EOF
}

if [[ $# -lt 1 || $# -gt 2 ]]; then
  usage
  exit 1
fi

VERSION="$1"
PUSH_TAG="false"

if [[ $# -eq 2 ]]; then
  if [[ "$2" != "--push" ]]; then
    usage
    exit 1
  fi
  PUSH_TAG="true"
fi

if [[ ! "$VERSION" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
  echo "Error: version must be semantic version format like 0.3.0"
  exit 1
fi

TAG="v${VERSION}"

if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "Error: run this script inside the repository."
  exit 1
fi

if [[ -n "$(git status --porcelain)" ]]; then
  echo "Error: working tree must be clean before creating a release tag."
  exit 1
fi

if git rev-parse "$TAG" >/dev/null 2>&1; then
  echo "Error: local tag ${TAG} already exists."
  exit 1
fi

if git ls-remote --exit-code --tags origin "refs/tags/${TAG}" >/dev/null 2>&1; then
  echo "Error: remote tag ${TAG} already exists on origin."
  exit 1
fi

echo "Building plugin ZIP for version ${VERSION}..."
./gradlew -PpluginVersion="${VERSION}" clean buildPlugin --no-daemon --console=plain

echo "Creating git tag ${TAG}..."
git tag -a "${TAG}" -m "Release ${TAG}"

if [[ "${PUSH_TAG}" == "true" ]]; then
  echo "Pushing ${TAG} to origin..."
  git push origin "${TAG}"
  echo "Done: ${TAG} pushed. GitHub release workflow should start automatically."
else
  echo "Done: ${TAG} created locally."
  echo "Push it when ready: git push origin ${TAG}"
fi

echo "Built ZIPs:"
ls -1 build/distributions/*.zip
