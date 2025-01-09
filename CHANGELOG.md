# Semantic Versioning Changelog

## [1.1.1](https://github.com/rafael-rollo/tuitr-api/compare/v1.1.0...v1.1.1) (2025-01-09)


### Bug Fixes

* **migration:** updates dataset to remove sensitive data ([fdcdae4](https://github.com/rafael-rollo/tuitr-api/commit/fdcdae4802bfe6dac08709c172e39b005a9fef2b))

# [1.1.0](https://github.com/rafael-rollo/tuitr-api/compare/v1.0.0...v1.1.0) (2024-09-25)


### Bug Fixes

* **ci:** creates a name for installation step ([c46b6a0](https://github.com/rafael-rollo/tuitr-api/commit/c46b6a043c9134b7924bd78610fa00f4df594f19))


### Features

* updates readme with instructions to run the release jar file ([921ff73](https://github.com/rafael-rollo/tuitr-api/commit/921ff73e07c1c0e1deb188f166525762c9f6d255))

# 1.0.0 (2024-09-25)


### Bug Fixes

* **build:** update makefile stages ([f1c6842](https://github.com/rafael-rollo/tuitr-api/commit/f1c6842416b654724f1d515aef3b42e9d84636f1))
* **ci:** updates installation command to follow the docs ([050be7c](https://github.com/rafael-rollo/tuitr-api/commit/050be7cb8ccd43499a12f840a6a3ec750fc2e7b7))
* **ci:** updates release rc to fix release branch ref ([749cc07](https://github.com/rafael-rollo/tuitr-api/commit/749cc076ee83a87fa0552672ed6fe8921527de66))
* **ci:** updates to set environment for workflow ([e5b40d0](https://github.com/rafael-rollo/tuitr-api/commit/e5b40d070b832d01d05b53e7e6d731d1567570c0))
* **ci:** updates to use semantic release action ([c5296c5](https://github.com/rafael-rollo/tuitr-api/commit/c5296c59cac8bdd046e83602f3150fed5c7f1d38))
* **ci:** updates workflow to fix actions error ([0267570](https://github.com/rafael-rollo/tuitr-api/commit/02675709a952171be73154859c87cb0a45164db7))
* **ci:** updates workflow to install missing plugins ([90cad0e](https://github.com/rafael-rollo/tuitr-api/commit/90cad0ee10abb4572439d06832c9abc9b24bddc8))
* **posts:** make the query order by creation date time desc ([ff0aca2](https://github.com/rafael-rollo/tuitr-api/commit/ff0aca261f00542b46c5b3519e7f43b68fe67f6a))
* **readme:** typo ([d465155](https://github.com/rafael-rollo/tuitr-api/commit/d465155585ec96a1d11fd0680b2d210ba1596646))
* **readme:** updates java version info ([b47f5a2](https://github.com/rafael-rollo/tuitr-api/commit/b47f5a2605512b68aade8d5f62310f3ecbe7769a))
* **security:** improves log message on commence config ([a92e268](https://github.com/rafael-rollo/tuitr-api/commit/a92e2687eaae2d6aee95e341db03d11c0f9aae82))
* **security:** updates path ignoring patterns ([70145b7](https://github.com/rafael-rollo/tuitr-api/commit/70145b73d906d6170faee575e4f89ea52ac73234))
* **serialization:** fix the date and date time default patterns for serialization ([2f5d0a4](https://github.com/rafael-rollo/tuitr-api/commit/2f5d0a4cad47089b60bed073b2a491b655e7218d))
* **username-validation:** remove not blanck validation inference ([6c15ff4](https://github.com/rafael-rollo/tuitr-api/commit/6c15ff4a27ba2a70d5aef05a5c85f47aa7c8725a))
* **users:** add fix in controller mappings ([170bd23](https://github.com/rafael-rollo/tuitr-api/commit/170bd23806b74bae9e66ccc32c432ddbdace2a60))
* **users:** add profile picture path data to users ([ab961d0](https://github.com/rafael-rollo/tuitr-api/commit/ab961d03bb5a9b68b2da9390a6c280e7c6ecb48e))
* **users:** add user profile picture on simple user output ([b4f69a6](https://github.com/rafael-rollo/tuitr-api/commit/b4f69a68aac0d3093140275a8bfa689f27d4da56))
* **users:** using Set to enforce integrity in followers/followings collection ([d5f0537](https://github.com/rafael-rollo/tuitr-api/commit/d5f0537a82eeef1c14bcb6ab0fb82c41a68d8c86))


### Features

* **auth:** add user authentication support ([280f373](https://github.com/rafael-rollo/tuitr-api/commit/280f3731a8b09e1c8d541b3cd65654acb120f433))
* **posts:** add feed loading feature ([d05b67c](https://github.com/rafael-rollo/tuitr-api/commit/d05b67c57f4cbb4b12d426c6b1d899b34ed6814e))
* **posts:** add list post of logged user feature ([f062595](https://github.com/rafael-rollo/tuitr-api/commit/f06259581d10ae93d9ed55f1fd59889c5176a222))
* **posts:** add post creation feature ([b9b2199](https://github.com/rafael-rollo/tuitr-api/commit/b9b21991b9c9382c3584fb054aaa6fc8cd9ea0f6))
* **posts:** add the foundation for the post features ([8a8de71](https://github.com/rafael-rollo/tuitr-api/commit/8a8de71eb0f7c7f532fa68dcbe3b781bacb2c47a))
* **users-domain:** added user, role, service anda repository implementation ([16eb500](https://github.com/rafael-rollo/tuitr-api/commit/16eb5008b17d68f76d15588c317a6df22f564c2f))
* **users:** add constructor that initializes with username ([fc98eb3](https://github.com/rafael-rollo/tuitr-api/commit/fc98eb3b13205c0386183b8d7cd13cbd1f9431d4))
* **users:** add delete account action implementation ([0ad25a2](https://github.com/rafael-rollo/tuitr-api/commit/0ad25a2b2db21c75ded5ab0946898698ecf477cc))
* **users:** add feature to unfollow users ([9c689ca](https://github.com/rafael-rollo/tuitr-api/commit/9c689ca0298a3d58308020e6bd57ea7479e9158b))
* **users:** add follow feature ([9a13ebe](https://github.com/rafael-rollo/tuitr-api/commit/9a13ebe7705bf328e42a8ba6abd1ff747b487092))
* **users:** add get user profile details action ([497ecc4](https://github.com/rafael-rollo/tuitr-api/commit/497ecc46eb19760dd74322186f37e9c234ee4106))
* **users:** add list followers feature ([f669c48](https://github.com/rafael-rollo/tuitr-api/commit/f669c48570ea56c3f7f755003a388f2a46166561))
* **users:** add remove followers feature ([855ba01](https://github.com/rafael-rollo/tuitr-api/commit/855ba010bfb63e572a9b6192ee39a99f076ff3c0))
* **users:** add signup action implementation ([ccd9750](https://github.com/rafael-rollo/tuitr-api/commit/ccd9750d117deaf168f5a4e9a515dd5240b12e74))
* **users:** add update profile action implementation ([c080d5a](https://github.com/rafael-rollo/tuitr-api/commit/c080d5a7e35d4bc066f021df51620176111ecb9b))
* **users:** add users additional info and followers/following relations ([583f2ee](https://github.com/rafael-rollo/tuitr-api/commit/583f2ee972c0ed83dd0b5ba2c124476b1067cd75))
* **users:** add users following list feature ([3972d8f](https://github.com/rafael-rollo/tuitr-api/commit/3972d8f9c112a41b07defe67165b3e7b2567b3a4))
