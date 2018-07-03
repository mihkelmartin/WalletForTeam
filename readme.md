
After cloning from GitHup repository
1. start local MongoDB and create new user using username/password from
application.properties of test resources of WalletForTeamBe.
db.createUser(
{
    user: "walletforteam",
    pwd: "murakas301polaarjoon",
    roles: [
              { role: "userAdminAnyDatabase", db: "admin" }
           ]
})
2. Build.