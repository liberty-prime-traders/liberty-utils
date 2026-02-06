#!/usr/bin/env bash

docker compose --env-file dev.env up -d "$@"
