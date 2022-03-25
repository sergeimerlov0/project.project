create sequence answer_seq start 1 increment 1;
create sequence answer_vote_seq start 1 increment 1;
create sequence badge_seq start 1 increment 1;
create sequence chat_seq start 1 increment 1;
create sequence comment_seq start 1 increment 1;
create sequence ignore_tag_seq start 1 increment 1;
create sequence message_seq start 1 increment 1;
create sequence question_seq start 1 increment 1;
create sequence question_viewed_seq start 1 increment 1;
create sequence related_tag_seq start 1 increment 1;
create sequence reputation_seq start 1 increment 1;
create sequence role_seq start 1 increment 1;
create sequence tag_seq start 1 increment 1;
create sequence tracked_tag_seq start 1 increment 1;
create sequence user_seq start 1 increment 1;
create sequence user_badges_seq start 1 increment 1;
create sequence user_favorite_question_seq start 1 increment 1;
create sequence bookmarks_seq start 1 increment 1;
create sequence vote_question_seq start 1 increment 1;

CREATE TABLE answer
(
    id                      BIGINT                      NOT NULL,
    persist_date            TIMESTAMP WITHOUT TIME ZONE,
    update_date             TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    question_id             BIGINT                      NOT NULL,
    user_id                 BIGINT                      NOT NULL,
    html_body               TEXT,
    is_helpful              BOOLEAN,
    is_deleted              BOOLEAN,
    is_deleted_by_moderator BOOLEAN,
    date_accept_time        TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_answer PRIMARY KEY (id)
);

CREATE TABLE badges
(
    id                    BIGINT NOT NULL,
    badge_name            VARCHAR(255),
    reputations_for_merit INTEGER,
    description           VARCHAR(255),
    CONSTRAINT pk_badges PRIMARY KEY (id)
);

CREATE TABLE bookmarks
(
    id           BIGINT                      NOT NULL,
    persist_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id      BIGINT                      NOT NULL,
    question_id  BIGINT                      NOT NULL,
    CONSTRAINT pk_bookmarks PRIMARY KEY (id)
);

CREATE TABLE chat
(
    id           BIGINT NOT NULL,
    title        VARCHAR(255),
    persist_date TIMESTAMP WITHOUT TIME ZONE,
    chat_type    SMALLINT,
    CONSTRAINT pk_chat PRIMARY KEY (id)
);

CREATE TABLE comment
(
    id                  BIGINT NOT NULL,
    text                VARCHAR(255),
    comment_type        SMALLINT,
    persist_date        TIMESTAMP WITHOUT TIME ZONE,
    last_redaction_date TIMESTAMP WITHOUT TIME ZONE,
    user_id             BIGINT NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);

CREATE TABLE comment_answer
(
    comment_id BIGINT NOT NULL,
    answer_id  BIGINT NOT NULL,
    CONSTRAINT pk_comment_answer PRIMARY KEY (comment_id)
);

CREATE TABLE comment_question
(
    comment_id  BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    CONSTRAINT pk_comment_question PRIMARY KEY (comment_id)
);

CREATE TABLE group_chat
(
    chat_id BIGINT NOT NULL,
    CONSTRAINT pk_group_chat PRIMARY KEY (chat_id)
);

CREATE TABLE groupchat_has_users
(
    chat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_groupchat_has_users PRIMARY KEY (chat_id, user_id)
);

CREATE TABLE message
(
    id                  BIGINT                      NOT NULL,
    message             TEXT,
    last_redaction_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    persist_date        TIMESTAMP WITHOUT TIME ZONE,
    user_sender_id      BIGINT                      NOT NULL,
    chat_id             BIGINT                      NOT NULL,
    CONSTRAINT pk_message PRIMARY KEY (id)
);

CREATE TABLE question
(
    id                  BIGINT                      NOT NULL,
    title               VARCHAR(255),
    description         TEXT,
    persist_date        TIMESTAMP WITHOUT TIME ZONE,
    user_id             BIGINT                      NOT NULL,
    last_redaction_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    is_deleted          BOOLEAN,
    CONSTRAINT pk_question PRIMARY KEY (id)
);

CREATE TABLE question_has_tag
(
    question_id BIGINT NOT NULL,
    tag_id      BIGINT NOT NULL,
    CONSTRAINT pk_question_has_tag PRIMARY KEY (question_id, tag_id)
);

CREATE TABLE question_viewed
(
    id           BIGINT NOT NULL,
    user_id      BIGINT,
    question_id  BIGINT,
    persist_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_question_viewed PRIMARY KEY (id)
);

CREATE TABLE related_tag
(
    id        BIGINT NOT NULL,
    main_tag  BIGINT NOT NULL,
    child_tag BIGINT NOT NULL,
    CONSTRAINT pk_related_tag PRIMARY KEY (id)
);

CREATE TABLE reputation
(
    id           BIGINT NOT NULL,
    persist_date TIMESTAMP WITHOUT TIME ZONE,
    author_id    BIGINT,
    sender_id    BIGINT,
    count        INTEGER,
    type         INTEGER,
    question_id  BIGINT,
    answer_id    BIGINT,
    CONSTRAINT pk_reputation PRIMARY KEY (id)
);

CREATE TABLE role
(
    id   BIGINT NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE single_chat
(
    chat_id     BIGINT NOT NULL,
    user_one_id BIGINT NOT NULL,
    use_two_id  BIGINT NOT NULL,
    CONSTRAINT pk_single_chat PRIMARY KEY (chat_id)
);

CREATE TABLE tag
(
    id           BIGINT NOT NULL,
    name         VARCHAR(255),
    description  VARCHAR(255),
    persist_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_tag PRIMARY KEY (id)
);

CREATE TABLE tag_ignore
(
    id             BIGINT NOT NULL,
    ignored_tag_id BIGINT NOT NULL,
    user_id        BIGINT NOT NULL,
    persist_date   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_tag_ignore PRIMARY KEY (id)
);

CREATE TABLE tag_tracked
(
    id             BIGINT NOT NULL,
    tracked_tag_id BIGINT NOT NULL,
    user_id        BIGINT NOT NULL,
    persist_date   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_tag_tracked PRIMARY KEY (id)
);

CREATE TABLE user_badges
(
    id        BIGINT NOT NULL,
    ready     BOOLEAN,
    user_id   BIGINT,
    badges_id BIGINT,
    CONSTRAINT pk_user_badges PRIMARY KEY (id)
);

CREATE TABLE user_entity
(
    id                  BIGINT                      NOT NULL,
    email               VARCHAR(255),
    password            VARCHAR(255),
    full_name           VARCHAR(255),
    persist_date        TIMESTAMP WITHOUT TIME ZONE,
    is_enabled          BOOLEAN,
    is_deleted          BOOLEAN,
    city                VARCHAR(255),
    link_site           VARCHAR(255),
    link_github         VARCHAR(255),
    link_vk             VARCHAR(255),
    about               VARCHAR(255),
    image_link          VARCHAR(255),
    last_redaction_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    nickname            VARCHAR(255),
    role_id             BIGINT                      NOT NULL,
    CONSTRAINT pk_user_entity PRIMARY KEY (id)
);

CREATE TABLE user_favorite_question
(
    id           BIGINT                      NOT NULL,
    persist_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id      BIGINT                      NOT NULL,
    question_id  BIGINT                      NOT NULL,
    CONSTRAINT pk_user_favorite_question PRIMARY KEY (id)
);

CREATE TABLE votes_on_answers
(
    id           BIGINT NOT NULL,
    user_id      BIGINT NOT NULL,
    answer_id    BIGINT NOT NULL,
    persist_date TIMESTAMP WITHOUT TIME ZONE,
    vote         VARCHAR(255),
    CONSTRAINT pk_votes_on_answers PRIMARY KEY (id)
);

CREATE TABLE votes_on_questions
(
    id           BIGINT NOT NULL,
    user_id      BIGINT,
    question_id  BIGINT,
    persist_date TIMESTAMP WITHOUT TIME ZONE,
    vote         VARCHAR(255),
    CONSTRAINT pk_votes_on_questions PRIMARY KEY (id)
);

ALTER TABLE answer
    ADD CONSTRAINT FK_ANSWER_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE answer
    ADD CONSTRAINT FK_ANSWER_ON_USER FOREIGN KEY (user_id) REFERENCES user_entity (id);

ALTER TABLE bookmarks
    ADD CONSTRAINT FK_BOOKMARKS_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE bookmarks
    ADD CONSTRAINT FK_BOOKMARKS_ON_USER FOREIGN KEY (user_id) REFERENCES user_entity (id);

ALTER TABLE comment_answer
    ADD CONSTRAINT FK_COMMENT_ANSWER_ON_ANSWER FOREIGN KEY (answer_id) REFERENCES answer (id);

ALTER TABLE comment_answer
    ADD CONSTRAINT FK_COMMENT_ANSWER_ON_COMMENT FOREIGN KEY (comment_id) REFERENCES comment (id);

ALTER TABLE comment
    ADD CONSTRAINT FK_COMMENT_ON_USER FOREIGN KEY (user_id) REFERENCES user_entity (id);

ALTER TABLE comment_question
    ADD CONSTRAINT FK_COMMENT_QUESTION_ON_COMMENT FOREIGN KEY (comment_id) REFERENCES comment (id);

ALTER TABLE comment_question
    ADD CONSTRAINT FK_COMMENT_QUESTION_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE group_chat
    ADD CONSTRAINT FK_GROUP_CHAT_ON_CHAT FOREIGN KEY (chat_id) REFERENCES chat (id);

ALTER TABLE message
    ADD CONSTRAINT FK_MESSAGE_ON_CHAT FOREIGN KEY (chat_id) REFERENCES chat (id);

ALTER TABLE message
    ADD CONSTRAINT FK_MESSAGE_ON_USERSENDER FOREIGN KEY (user_sender_id) REFERENCES user_entity (id);

ALTER TABLE question
    ADD CONSTRAINT FK_QUESTION_ON_USER FOREIGN KEY (user_id) REFERENCES user_entity (id);

ALTER TABLE question_viewed
    ADD CONSTRAINT FK_QUESTION_VIEWED_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE question_viewed
    ADD CONSTRAINT FK_QUESTION_VIEWED_ON_USER FOREIGN KEY (user_id) REFERENCES user_entity (id);

ALTER TABLE related_tag
    ADD CONSTRAINT FK_RELATED_TAG_ON_CHILD_TAG FOREIGN KEY (child_tag) REFERENCES tag (id);

ALTER TABLE related_tag
    ADD CONSTRAINT FK_RELATED_TAG_ON_MAIN_TAG FOREIGN KEY (main_tag) REFERENCES tag (id);

ALTER TABLE reputation
    ADD CONSTRAINT FK_REPUTATION_ON_ANSWER FOREIGN KEY (answer_id) REFERENCES answer (id);

ALTER TABLE reputation
    ADD CONSTRAINT FK_REPUTATION_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES user_entity (id);

ALTER TABLE reputation
    ADD CONSTRAINT FK_REPUTATION_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE reputation
    ADD CONSTRAINT FK_REPUTATION_ON_SENDER FOREIGN KEY (sender_id) REFERENCES user_entity (id);

ALTER TABLE single_chat
    ADD CONSTRAINT FK_SINGLE_CHAT_ON_CHAT FOREIGN KEY (chat_id) REFERENCES chat (id);

ALTER TABLE single_chat
    ADD CONSTRAINT FK_SINGLE_CHAT_ON_USERONE FOREIGN KEY (user_one_id) REFERENCES user_entity (id);

ALTER TABLE single_chat
    ADD CONSTRAINT FK_SINGLE_CHAT_ON_USETWO FOREIGN KEY (use_two_id) REFERENCES user_entity (id);

ALTER TABLE tag_ignore
    ADD CONSTRAINT FK_TAG_IGNORE_ON_IGNOREDTAG FOREIGN KEY (ignored_tag_id) REFERENCES tag (id);

ALTER TABLE tag_ignore
    ADD CONSTRAINT FK_TAG_IGNORE_ON_USER FOREIGN KEY (user_id) REFERENCES user_entity (id);

ALTER TABLE tag_tracked
    ADD CONSTRAINT FK_TAG_TRACKED_ON_TRACKEDTAG FOREIGN KEY (tracked_tag_id) REFERENCES tag (id);

ALTER TABLE tag_tracked
    ADD CONSTRAINT FK_TAG_TRACKED_ON_USER FOREIGN KEY (user_id) REFERENCES user_entity (id);

ALTER TABLE user_badges
    ADD CONSTRAINT FK_USER_BADGES_ON_BADGES FOREIGN KEY (badges_id) REFERENCES badges (id);

ALTER TABLE user_badges
    ADD CONSTRAINT FK_USER_BADGES_ON_USER FOREIGN KEY (user_id) REFERENCES user_entity (id);

ALTER TABLE user_entity
    ADD CONSTRAINT FK_USER_ENTITY_ON_ROLE FOREIGN KEY (role_id) REFERENCES role (id);

ALTER TABLE user_favorite_question
    ADD CONSTRAINT FK_USER_FAVORITE_QUESTION_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE user_favorite_question
    ADD CONSTRAINT FK_USER_FAVORITE_QUESTION_ON_USER FOREIGN KEY (user_id) REFERENCES user_entity (id);

ALTER TABLE votes_on_answers
    ADD CONSTRAINT FK_VOTES_ON_ANSWERS_ON_ANSWER FOREIGN KEY (answer_id) REFERENCES answer (id);

ALTER TABLE votes_on_answers
    ADD CONSTRAINT FK_VOTES_ON_ANSWERS_ON_USER FOREIGN KEY (user_id) REFERENCES user_entity (id);

ALTER TABLE votes_on_questions
    ADD CONSTRAINT FK_VOTES_ON_QUESTIONS_ON_QUESTION FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE votes_on_questions
    ADD CONSTRAINT FK_VOTES_ON_QUESTIONS_ON_USER FOREIGN KEY (user_id) REFERENCES user_entity (id);

ALTER TABLE groupchat_has_users
    ADD CONSTRAINT fk_grohasuse_on_group_chat FOREIGN KEY (chat_id) REFERENCES group_chat (chat_id);

ALTER TABLE groupchat_has_users
    ADD CONSTRAINT fk_grohasuse_on_user FOREIGN KEY (user_id) REFERENCES user_entity (id);

ALTER TABLE question_has_tag
    ADD CONSTRAINT fk_quehastag_on_question FOREIGN KEY (question_id) REFERENCES question (id);

ALTER TABLE question_has_tag
    ADD CONSTRAINT fk_quehastag_on_tag FOREIGN KEY (tag_id) REFERENCES tag (id);