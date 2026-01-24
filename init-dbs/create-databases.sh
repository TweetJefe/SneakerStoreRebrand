#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE SCHEMA IF NOT EXISTS users_schema AUTHORIZATION postgres;
    CREATE SCHEMA IF NOT EXISTS carts_schema AUTHORIZATION postgres;
    CREATE SCHEMA IF NOT EXISTS orders_schema AUTHORIZATION postgres;
    CREATE SCHEMA IF NOT EXISTS products_schema AUTHORIZATION postgres;
    CREATE SCHEMA IF NOT EXISTS shipments_schema AUTHORIZATION postgres;
EOSQL