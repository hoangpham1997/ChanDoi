import { Injectable } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';

import { Principal } from '../auth/principal.service';
import { AuthServerProvider } from '../auth/auth-jwt.service';
import { SERVER_API_URL } from 'app/app.constants';
import { map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class LoginService {
    constructor(
        private languageService: JhiLanguageService,
        private principal: Principal,
        private authServerProvider: AuthServerProvider,
        private http: HttpClient
    ) {}

    login(credentials, callback?) {
        const cb = callback || function() {};

        return new Promise((resolve, reject) => {
            this.authServerProvider.login(credentials).subscribe(
                data => {
                    this.principal.identity(true).then(account => {
                        // After the login the language will be changed to
                        // the language selected by the user during his registration
                        if (account !== null) {
                            this.languageService.changeLanguage(account.langKey);
                        }
                        resolve(data);
                    });
                    return cb(data);
                },
                err => {
                    this.logout();
                    reject(err);
                    return cb(err);
                }
            );
        });
    }

    preLogin(req: any) {
        return this.http.post(SERVER_API_URL + 'api/authenticate', req, { observe: 'response' });
    }

    loginWithToken(jwt, rememberMe) {
        return this.authServerProvider.loginWithToken(jwt, rememberMe);
    }

    storeAuthenticationToken(jwt, rememberMe) {
        this.authServerProvider.storeAuthenticationToken(jwt, rememberMe);
    }

    logout() {
        sessionStorage.removeItem('searchReportSession');
        this.authServerProvider.logout().subscribe();
        this.principal.authenticate(null);
        sessionStorage.clear();
    }

    checkLogin(req: any) {
        return this.http
            .post(SERVER_API_URL + 'api/admin/authenticate', req, { observe: 'response' })
            .pipe(map(authenticateSuccess.bind(this)));

        function authenticateSuccess(resp) {
            const bearerToken = resp.headers.get('Authorization');
            if (bearerToken && bearerToken.slice(0, 7) === 'Bearer ') {
                const jwt = bearerToken.slice(7, bearerToken.length);
                this.storeAuthenticationToken(jwt, req.rememberMe);
                return jwt;
            }
        }
    }
}
