create sequence bookmarks_seq start 1 increment 1;

ALTER TABLE bookmarks
    ADD persist_date TIMESTAMP WITHOUT TIME ZONE;
