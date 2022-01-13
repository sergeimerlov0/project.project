ALTER TABLE votes_on_answers
    ADD CONSTRAINT UQ_VOTES_ON_ANSWERS UNIQUE(user_id, answer_id);
