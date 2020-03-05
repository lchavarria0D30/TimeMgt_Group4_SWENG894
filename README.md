# To run the auth service.
clone the repo.

  
 
 
 * For linux based os:
 1. switch to bashiirUserUathFeature branch (git checkout bashiirUserUathFeature)
1. cd to userAuth/userAuth.
1. Open terminal and run:  ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev 
* For windows os
1. manually install maven. 
1. then run mvn spring-boot:run -Dspring-boot.run.profiles=dev.

# To sync the personal branch with `TestMaster` branch
Typically there are two ways to sync your personal branch with other (`TestMaster`) branch: `merge` or `rebase` (For the difference between `merge` and `rebase`, and other details, please check https://www.atlassian.com/git/tutorials/merging-vs-rebasing)
1. `merge`: 
   1. Firstly, please sync your local repository:
      1. `git checkout your-branch`
      1. `git fetch origin`
      1. `git pull`
   1. Then, merge the target branch into your own branch:
      * `git merge TestMaster (or other branch name)`
   1. Then, push the changes into central repository:
      * `git push`
   1. Done. Your branch is syned with the target branch.
1. `rebase`
   1. Firstly, please sync your local repository (same with `merge`):
      1. `git checkout your-branch`
      1. `git fetch origin`
      1. `git pull`
   1. Then, rebase the current branch with the target branch:
      * `git rebase TestMaster (or other branch name`
   1. Now you should push the changes into central repository:
      * `git push`
   1. Done. Your branch is synced with the target branch.
   
Besides CLI (Command-line interface), we can also sync the branch via github website:
1. Open the target branch (`TestMaster`) in the web browser;
1. Click `New Pull Request` button;
1. In the `Compare changes` page, click the `Base` dropdown box, choose your branch as the base branch;
1. If there are any unsynced commits, the page will show the differences, and let you create a new PR with proper name and description;
1. After the PR is created, you should be able to merge the PR into your branch. Then your branch is synced successfully.
