import Event from '../event';

export default class Http {

    static NO_ACCESS = 'no_access';
    #api;
    #headers;

    constructor(api = '', headers = []) {
        if (new.target === Http) {
            throw new Error('本类不能实例化');
        }
        this.#api = api;
        this.#headers = headers;
    }

    execute(path: string, type = 'get', data): Promise<Response> {
        return new Promise((okCallback, errorCallback) => {

            let params = {
                method: type.toLocaleUpperCase(),
                headers: {'Content-Type': 'application/json', ...this.#headers, ...this.getHeaders()},
            };

            if (data) {
                params.body = JSON.stringify(data);
            }

            fetch(path, params).then((response) => {
                response.json().then((resData) => {
                    if (response.ok) {

                        okCallback(resData);
                    } else {

                        if (resData.status == 401 || resData.status == 403) {
                            if (this.noAccess) {
                                this.noAccess(resData.status);
                            } else {
                                Event.emit(Http.NO_ACCESS);
                            }
                        }
                        errorCallback && errorCallback({
                            code: resData.status,
                            message: resData.message ? resData.message : '服务器开了个小差',
                        });
                    }

                }).catch((error) => {
                    errorCallback && errorCallback({code: 500, message: '服务器开了个小差'});
                    return;
                });
            }).catch((error) => {
                errorCallback && errorCallback({code: 500, message: '网络异常,请检查网络' + error});
            });
        });
    }

    get(path: string, isAbs = false): Promise<Response> {
        return this.execute(isAbs ? path : this.#api + path);
    }

    get(path: string): Promise<Response> {
        return this.execute(this.#api + path);
    }

    post(path: string, data): Promise<Response> {
        return this.execute(this.#api + path, 'post', data);
    }

    put(path: string, data): Promise<Response> {
        return this.execute(this.#api + path, 'put', data);
    }

    delete(path: string): Promise<Response> {
        return this.execute(this.#api + path, 'delete');
    }
}
