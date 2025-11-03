-- Add role and updated_at to users
ALTER TABLE users
    ADD COLUMN role ENUM('USER', 'ORGANIZER', 'ADMIN') NOT NULL DEFAULT 'USER' AFTER password_hash,
    ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_at;

-- Add timestamps and constraints to wallets
ALTER TABLE wallets
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP AFTER balance,
    ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_at,
    ADD UNIQUE INDEX idx_wallets_user_unique (user_id);
-- Add indexes to users

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);