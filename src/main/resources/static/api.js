async function authorizedFetch(url, options = {}) {
    const token = localStorage.getItem('token');
    const headers = {
        ...options.headers,
        'Authorization': token ? `Bearer ${token}` : '',
        'Content-Type': 'application/json'
    };

    return fetch(url, {
        ...options,
        headers
    });
}
