drop view if exists v_ranks_current;
create view v_ranks_current(sid, sname, gid, gname, hacker, rank, score, date) as
SELECT s.id AS sid,
       s.name AS sname,
       g.id AS gid,
       g.name AS gname,
       s.hacker,
       rc.rank,
       rc.score,
       rc.date
FROM students s
         LEFT JOIN groups g ON s.groupp = g.id
         LEFT JOIN ranks_current rc ON s.id = rc.student;
