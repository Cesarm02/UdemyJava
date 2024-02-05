db.createUser({
        user: 'root',
        pwd: 'toor',
        roles: [
            {
                role: 'readWrite',
                db: 'testDB',
            },
        ],
    });
db.createCollection('app_users', { capped: false });

db.app_users.insert([
    {
        "username": "ragnar777",
        "dni": "VIKI771012HMCRG093",
        "enabled": true,
        "password_not_encrypt": "s3cr3t",
        "password": "$2a$10$WdoGMe.9FxeS5phhw0UMHuOaFoXGll4RqhYKCSh24QaNHdG0uek96",
        "role":
        {
            "granted_authorities": ["ROLE_USER"]
        }
    },
    {
        "username": "heisenberg",
        "dni": "BBMB771012HMCRR022",
        "enabled": true,
        "password_not_encrypt": "p4sw0rd",
        "password": "$2a$10$nlAti.rkHhhpTKHLUNoiQOKH/K9RYzN8biX2IVz0E.jE0a0cOkjuq",
        "role":
        {
            "granted_authorities": ["ROLE_USER"]
        }
    },
    {
        "username": "misterX",
        "dni": "GOTW771012HMRGR087",
        "enabled": true,
        "password_not_encrypt": "misterX123",
        "password": "$2a$10$.pIXR1m2EUbzYHoGB8RSI.GtYwFNHQQEiphRdh.n1EM0yowz7dycy",
        "role":
        {
            "granted_authorities": ["ROLE_USER", "ROLE_ADMIN"]
        }
    },
    {
        "username": "neverMore",
        "dni": "WALA771012HCRGR054",
        "enabled": true,
        "password_not_encrypt": "$2a$10$HiT643Tpmgf/hqA50784S.DpbwwhAGf4trKX7b.uQaMmDOkOdQOXq",
        "password": "4dmIn",
        "role":
        {
            "granted_authorities": ["ROLE_ADMIN"]
        }
    }
]);