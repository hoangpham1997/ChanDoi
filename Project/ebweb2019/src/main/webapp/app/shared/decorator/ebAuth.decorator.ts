export function ebAuth(requiredAuthorities: string[]) {
    return function(target: any, key: any, descriptor: any) {
        const oldFunc = descriptor.value;
        descriptor.value = function(...args: any[]) {
            event.preventDefault();
            for (let i = 0; i < requiredAuthorities.length; i++) {
                if (
                    (this.account && this.account.authorities.includes(requiredAuthorities[i])) ||
                    (this.currentAccount && this.currentAccount.authorities.includes(requiredAuthorities[i]))
                ) {
                    return oldFunc.apply(this, args);
                }
            }
        };
    };
}
