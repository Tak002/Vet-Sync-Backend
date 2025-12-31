(() => {
    const STORAGE_KEY = "swagger_access_token";

    // ✅ 너의 로그인 엔드포인트에 맞게 수정
    const LOGIN_PATH_CANDIDATES = [
        "/auth/login",
        "/login"
    ];

    function saveToken(token) {
        localStorage.setItem(STORAGE_KEY, token);
    }

    function loadToken() {
        return localStorage.getItem(STORAGE_KEY);
    }

    function authorizeIfPossible() {
        const token = loadToken();
        if (!token) return;
        const bearerValue = token.startsWith("Bearer ") ? token : `Bearer ${token}`;

        const ui = window.ui;
        if (!ui || !ui.getSystem) return;

        try {
            ui.getSystem().authActions.authorize({
                bearerAuth: {
                    name: "bearerAuth",
                    schema: { type: "http", scheme: "bearer", bearerFormat: "JWT" },
                    value: bearerValue
                }
            });
        } catch (e) {
            // ignore
        }
    }

    // ✅ Swagger UI 로드 후 기존 토큰 자동 적용
    const wait = setInterval(() => {
        if (window.ui && window.ui.getSystem) {
            clearInterval(wait);
            authorizeIfPossible();
        }
    }, 200);

    // ✅ 로그인 "응답"에서 accessToken 뽑아서 저장 + 즉시 authorize
    const origFetch = window.fetch;
    window.fetch = async (...args) => {
        const res = await origFetch(...args);

        try {
            const url = typeof args[0] === "string" ? args[0] : args[0]?.url;
            if (!url) return res;

            const matched = LOGIN_PATH_CANDIDATES.some(p => url.includes(p));
            if (!matched) return res;

            const cloned = res.clone();
            const data = await cloned.json().catch(() => null);

            // ✅ 응답 형태에 맞게 accessToken 위치 보정
            const token =
                data?.accessToken ??
                data?.data?.accessToken ??
                data?.result?.accessToken;

            if (token) {
                saveToken(token);
                authorizeIfPossible();
            }
        } catch (e) {
            // ignore
        }

        return res;
    };
})();
