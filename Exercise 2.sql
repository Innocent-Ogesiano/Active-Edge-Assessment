SELECT t1.id, t1.name, t1.age FROM t1
WHERE NOT EXISTS (
	SELECT * FROM t2
    WHERE t1.id = t2.id and t1.name = t2.name and t1.age = t2.age
)
ORDER BY t1.id;