/*
-- =============================================
-- Author:		dieunn
-- Create date: 20.06.2020
-- Description:	Báo cáo Sổ theo dõi thanh toán bằng ngoại tệ
EXEC Proc_SA_SoTheoDoiThanhToanBangNgoaiTe @FromDate = '2020-01-01', @ToDate = '2020-06-18', @TypeMoney = 'VND', @Account = '136', @AccountObjectID = ',32e7d0b9-e929-864b-80d7-002f4e86262c,10682bb4-7bd3-9841-8742-0035ea589763,dd581a1e-3eba-42ee-a4c4-00469e08e515,92acad33-0b44-c843-aeaa-011b7603527f,c45b5525-e9f6-214f-b1ef-013f364ae985,5468d2e0-293b-ff49-97d6-0194ef60120c,25d47e94-00ac-a64e-8118-01d7bd0ceae3,477f7c55-8723-4eb0-84e9-0232b64c5bd2,477f7c55-8723-4eb0-84e9-0232b64c5bd8,317d95e9-0e47-3e4a-8740-027628c2d940,86b9c43f-ff7d-434c-9182-02dee559b9f9,62919b28-77d0-754d-adc5-02f87867ec5f,b66d95bd-1c1f-9b4d-a0b5-033657f7745b,80128e4a-dafb-8441-81c8-034a1dee6772,de571751-f3aa-3241-b655-039aa18bdcc0,0d403039-1644-864e-892b-03b4d444fb63,6c7bc9fb-c2e5-594e-84d0-03ccf9b85cc5,04decca6-da3b-1440-a794-03d086cec968,446324b9-f6b3-8c4e-b27e-043ada9eda58,5bfdd064-6688-1741-a7c1-043de612763c,c6db4815-8119-b64b-bf31-0445ef56ab93,aea63f31-2069-4546-894a-0454e5acf235,3874d559-ead4-43d0-8610-0473f36ebd5e,4d947ca6-8206-584c-a56a-04888f1994b4,0a25cde4-225f-4249-bf78-04bd75036844,8b9f56cd-a228-7a42-8e98-04f0edbc9649,f99f32d1-6242-f948-b5ca-052bf8d63e79,08bfda5d-75f4-9049-a183-057920462c56,37105fc8-1555-7548-a0a6-05c27c2b271c,3349f04e-d235-4088-af19-05fd66255647,5f3fe525-dc76-f449-8b8b-065e2059e0df,0df8af37-30f6-5144-ac12-06cb38e3d7b1,fb680408-3508-8949-b307-070e603df2ec,2ef0fe5a-d932-584a-a979-07b089e7add7,41d5e259-3d2f-c14e-b68a-07e48fffae24,be770676-47bf-4641-a48d-08a2162bfc79,31ffdb8e-b8b2-40d7-85c4-08ac66b51084,1c0ff2aa-c529-d94f-8540-08da242d16c1,69259d23-53ab-be49-bc93-093d3d95b39e,e68d64cf-f8e4-453a-8985-09865bfb18d3,7031bf47-8cf2-ba40-a1af-09a596836f14,5d9fc1a5-5be3-4936-bf35-09b8c52132bd,82349e09-3176-b641-b7db-09c19d93acfa,58e504d6-5f30-8146-92f5-0a6d85f2e251,64201bbc-f8a2-5744-a643-0a850af20a7f,113030a4-5d23-4c52-9faf-0ad3cb12528c,ed040881-2b30-47d2-92b0-0b11dbb27eb8,ac8f6bd6-9971-d54e-8ae8-0b4a2fe1b3c7,47e582ea-01b1-3644-8ee3-0c6e02951e13,05e64c11-d667-f544-a4e9-0d46499999d0,c43c3128-beb3-4425-9811-0e48f552f86a,71136c2f-895b-1a47-aab3-0ebc38dd919a,6b287b62-50e1-c449-8ea1-0ee51e9f4c43,7708c5f5-464d-cc48-9280-0f0f8d9b0a89,bfda8265-c1e6-614e-a634-0f85174722a6,4a1c6ac3-142e-4adc-836a-1007572a1048,ec206bbc-bbfa-df4a-ba2d-10394ab1c88d,a432291d-09a4-44b7-8adb-10d5621092cc,ffeef0bf-c5d1-5a4d-a4f3-115e9e647bbb,9f9c5aa6-39f7-5a4f-b943-115ef494dcc6,f3b29986-a585-884c-88d7-1171a31cacda,14ac01e6-0229-a04b-a909-119623e639c5,d350c037-024a-d24a-860c-11bf63786b6f,7688f2b8-9e9e-40ea-a2a2-11c9944f5b1e,641112a4-5c0e-f145-b6fc-123b388b7dd2,37fb9ef4-419c-0f44-a870-126af0980989,b1a9fb52-10a9-47db-99d8-12b15b2a1244,36b2322b-8a8e-3f47-a69c-139ecfdf019c,31ab214d-3949-d944-a333-13e069b93aa5,bf6dfa38-86ed-474a-93d4-1492fad8b162,db6a5789-1ec7-ab4b-bd58-14eb8c4215da,18054ba2-af97-6a47-9535-15a61bc4cf69,4763bd9b-964f-dc4b-b321-15d4dcdc74e8,cc05a39a-225f-3845-85fc-166a3c504247,678c6c62-3e64-db4e-8964-16c6fc25902c,420031a7-d5fe-0b47-ae95-17198a002253,0423771d-5ab3-b64d-8f10-176e4e7d94f3,65bbd460-7090-434c-bf05-17a4c3bd2dc3,f218456b-3bb3-7a4f-b8b4-17d6b8759ac5,302195bc-f4a2-4a4c-a205-184ce48771a0,03cac12b-11bd-c040-9f46-1868b3a419dd,e3c16b9d-6246-314e-bc8c-19a404907ed9,509755cf-e407-0b47-a4ae-1ab7e13d0c24,fbfa3e0d-35d2-a24f-b6ec-1acf02b7d690,96fceae4-1de9-e048-b3e0-1b4d23ec3619,c23348e0-b8ba-a54b-8000-1bf9f6e05548,dfc1e32b-23eb-194b-b25c-1c08d9d22679,9eb674ab-61d1-084f-aadc-1c2a14e4e591,7fa9e84a-9e3b-c947-9457-1c58682a8cfe,db9ab6a0-b78a-954c-892a-1c5d403d52a5,fc2f3ead-315f-c746-9845-1c731da0efc6,69cfe9ac-cb36-3f47-8851-1cb90829b87a,8cfd11ca-3af3-4b4a-a57a-1cb94f0e5012,ae04f331-9771-3d4c-bacf-1e259dd755dd,500e1191-19c7-e449-9bdf-1e41a9dffc27,5e3ac66b-ff46-f74b-9c68-1ee5e3670781,f17d88d5-732a-ba48-8610-1f20e7b6bde7,357532fb-153f-e14b-ad4d-1f457a66ee59,281d7173-6209-7646-a79d-1fa010e9b04a,1230b9fe-8341-c849-96d5-1fa6e3c6864a,b70f7dd5-0874-8f4f-ac5e-200d286fa0ea,28459d7c-3cec-1749-b1c2-202d3a0caac2,378af48b-a454-b64e-8691-20c9dc32d554,0b61aff5-f212-8342-a199-20f7a30e4105,1c6b59e5-a22e-e947-9640-2124db4ec95b,ef4b68d8-3701-f646-98b4-21742f411159,74c27226-2649-224d-a740-2189ffee4eee,33bd2c7f-a1f5-ed4a-a820-219368273956,c31b62b2-aa2b-884f-ad35-21cb00e1be7e,f1dd2d14-ae65-5448-92f0-226d55890d4e,7a996b4b-1c03-b74c-84f5-22e254d27e55,b1d5ae66-b62c-f649-aa89-24a231b60a8b,95508fdd-cd86-684c-99eb-24cded6be934,f4270d13-b799-3542-9004-252383570d6f,b40036a6-eeb4-4e45-87e0-26162d1e1fce,78fa56b6-09e0-6543-9d98-264eb80b48ca,527ed75b-7513-6e4d-93b2-2663a0232970,f2e62478-68f4-0842-9263-2665f027458e,43093875-0d7b-0142-9bf4-2696b2cf184d,12c28d01-4bdd-eb45-a169-269f65394834,2b35bbe1-c7ab-9246-9617-272ebc5b7443,de1693fc-8d93-4048-ada8-27a46f6285b8,a6341587-3ff0-244c-949f-27b437b53527,d2a6e625-d3a0-0d40-be51-27c0ab29abcf,de95cf43-3fc6-974e-b274-27c9a1b8e478,73bb26cc-a3f2-bc42-8af9-285620b13b0e,e434f204-0122-4341-864a-285d5fd8e2e0,540cdcf5-a28c-c546-831c-28a86d28d17a,f4eab496-9d7b-954f-bf5e-28af3bfcc09d,abdf7ba3-12ad-7c4e-aa5f-28d0df6c541a,5d7dd2de-9ac2-aa4f-8c5d-2907f88439a6,65e423cd-0dfa-6343-a7e3-29a000e2608b,ed3ea876-8a27-a34d-b940-29cb814178ab,015f4937-535e-2c4d-8729-2a333415e132,36558085-3e37-a54d-bf3a-2ad18b3245d8,4f3843c1-8e45-1643-92b2-2add9df32e21,778f4cc3-1ab9-a945-9b4c-2bd115b627c0,1127e147-240f-774b-af37-2c229e664a49,51d22c44-42e7-874a-93b9-2d50e387c723,0dc3fb0d-1720-5a48-bdc5-2e0c43632bd5,4d5ca564-131a-1a44-87a9-2e26e140bd6c,d5ea724f-b082-a049-b95f-2ef3a96d6a6c,f1f6c70c-a08b-1842-acf3-2f0d1c7a3dbf,521bae37-510f-4c49-baea-2f74ffc803df,201fb933-2c0e-6647-a74a-2f8ecc64034d,a1bd7f5f-1d77-8945-80fb-3004ca79c4d1,06c5d543-2415-734e-9bfc-30e26a067cde,d8427238-55c9-bc4c-b245-30e45b2a985a,3a15fe72-edd4-2442-b006-312fe2cd20a5,d56641e2-7a60-5247-906a-326207f7a5b2,a42769f7-cb60-1746-8a28-32a0492f674e,e62b652b-72df-4621-92fd-32ad44eca21a,dc8f2922-759a-5d48-88da-32f6ee5a87b4,c1d65bf1-8e63-4e78-88bf-32fae50e97a2,550fb807-a969-42ca-99b5-32fe8b217393,6422f01a-6d3c-7947-b7e9-3309f354ba06,8f2b9de4-9ab5-411a-aff5-3338f88e536e,5debe53f-5c3a-9141-82a8-33a5ff8761fa,952a5e01-f04e-ff47-bc3a-33cb49d0822c,04ad2855-59ab-4799-aeff-34060cff3bae,8f00f3d7-5b88-284a-ba1f-345cf067141b,9260055d-481e-4341-97e3-35107b81d5cc,6194d72e-0661-8f49-9756-3593b8d17632,cde662ab-d237-514c-966f-35c5d9b87ad5,3a5230ec-4ed2-9f46-a67f-35d64cccde42,e511b204-0c81-2342-b8c1-36a17622bcab,63edf864-8073-1a45-b59e-37410f545e61,5d998a07-2dc1-0243-90b0-379a5727ebe9,f53a70c5-6b96-144d-a23d-37a7a92833e3,efe5a2cc-68d2-b64a-bf9f-37bdfa99a927,af25d0ca-2447-784e-85a1-38311a8ca5aa,65a0e68b-7a24-ca46-8eab-39bbac0206bd,58690741-f115-854e-a86e-39f7b00974fc,fe8124ed-8614-9f43-9dd6-3a03771d069d,1fa9b84f-71ba-0847-830c-3a12520858b2,3f7c376a-c55d-9341-ac12-3a98c73da4c6,0bd8e187-95fb-dd4c-af52-3ad3dc05eb5f,7824595d-de25-1f40-831a-3c78b2ecc6ab,f0ec0fa5-49fc-2445-b18a-3d1fb0910adf,bfeb8f9e-da9e-124e-bc08-3d726fdfc7b7,7f11a3b5-45cd-a649-b310-3d980a75c294,91b3d9ac-dd55-ac4f-b2ff-3e06368042a0,8ab64933-696c-724d-b5bb-3e57e61ab416,c27e2a42-8e40-374e-b7ae-3e78b1023456,94ad4760-439e-f149-92e5-3eba7abb184e,cf1b4fd4-bb12-2b4c-897c-3f2f95d0e096,9b054f05-3a63-6b4c-bcb1-3f856e9718ec,4a4b4b0c-5d55-e54a-9bd1-3fa047980d68,a81dcebe-749a-e84f-a855-3fddbd52ba2b,21577936-d6e6-9440-bff7-412703f9b7ad,7c630e43-b880-eb4f-9117-4136b043a376,e63d5025-ca07-694a-912b-421018eb1441,b5c32614-d2ce-704d-b2a5-425dda9383e1,b44ac4cf-9ec8-4b40-898d-4282c247aa9b,f84c1212-c7e4-e84b-bd9f-42c430ce303c,5f04892b-fd0f-2047-9aa2-436dbaf72555,239b431c-5323-1640-b022-4493a8695be3,8e95500d-ef95-c045-8413-44ad6fb4330f,f4406211-a31b-5746-bfba-450a4905809c,4c140c98-24e4-f143-a3c5-45352cf504d3,c6a4a464-541b-b54b-a5d3-45811e6b7a21,ce5ebdf5-b0c2-be4c-93a7-45b411ae5974,78d48e31-e07f-5344-b653-45b83dcc1711,3c8570dd-dd73-414a-94bb-46144ee2886c,03ede644-277e-d240-a49f-46bca33f6dad,0816004e-04a5-4243-aaba-46d2c40ddcc1,40d7f934-6815-174b-aaa9-47cb97613fcb,7fde162c-578f-d145-9fcd-47e86ca02051,b2554c66-0cef-f34e-899b-48077f36a692,d525f48b-a743-a943-88c2-48e9a61b2b66,652b3118-8733-d145-9f19-4aa6c787bedc,193565b9-f1d1-974f-bffd-4aff6a01efa2,1acd7406-9f10-ac44-8f09-4b17817a03b6,e915132f-b6aa-0e4e-9461-4b5e7c3aa826,b8863fe8-ad88-e342-9072-4ba44725ab27,64accae4-83e5-234b-9ed5-4be61a805d72,769002e4-7211-8847-aa8b-4c2006e28d40,b3c66cf7-7490-5947-941b-4dc5f73c8541,249121d6-f6f7-2c47-9113-4e9964f28a3c,0a7956b6-5702-ba43-997d-4ec3f7668d80,5a25fb3a-38fc-6e4a-af1e-4f035b2eeccd,1ea732be-f20e-274c-b9bf-4f32f7345617,41cce72f-d8cb-b742-832d-4f429aa02c18,a3090b8f-712e-ae41-93bb-512fcdd9e0c3,7cbca0ef-97e8-dc48-9381-5155e71528bc,5a3a84e6-a2a2-5d45-bcb4-51b669a21e0e,9a0f5023-ad3c-8249-bfbc-521fec402ce6,1dca9713-ec63-8640-9fce-52cbb3944c2e,27bff75e-de5c-744e-b8be-52fe38f67a77,ef1fcc44-1d09-9641-a5d6-5304fe994599,5dfdb359-6a7f-5d42-80fb-538c50483981,c83fb44a-17c4-ad40-ad0c-539b25a32c1d,5889d902-c586-3c42-a86f-54271e0dca3d,ab208e3b-1d1f-464a-85de-546d36d033dc,3b948294-8af1-bc4d-8264-54a4c754ae47,5eb18d44-2c6f-c243-b8f9-54de6353c3cf,ea04190d-683f-0444-b51f-552452159a13,23d8cc33-c72b-fe4c-8e9e-55733c584739,7eb8e3d7-bccf-1943-8fcd-55f92f138e2c,147ff8a8-5e8a-a44e-9b9a-567427462757,6bd2f0f1-49cd-6044-ad90-56fa529f958f,330701b8-c8fe-ce4d-a43f-57164361241f,ea03655e-fa36-1740-bd6d-574bb1562f5b,776e7e3b-b4f7-cd4b-b75c-57650fb76fdb,8688340d-def7-c74c-8936-5a5cec020e2f,20391346-1686-e54d-a951-5b2b2a20a754,91d14110-9f02-b14c-9838-5b493825a8ec,d33c5eb8-6f66-b149-9a79-5b89c36047b4,33f2d821-d6b3-b148-a901-5be56c761642,f713643f-231e-224b-92b2-5c35715c1dee,17d6ea92-49cc-654c-87eb-5c74f3d96ab1,ebfadd07-74b4-d847-9dd3-5cb0e204c168,8cecc709-80ee-6648-961b-5d9a37427730,117a9b62-41d1-8849-b8ea-5db717380e87,3df76c90-2abb-fe4d-86c2-5ec7d9544498,7a51adba-04db-4540-8be1-5ecaa7a2d464,9b291bb8-5356-534e-b9d1-5f01e7979e10,953f2362-f5cd-e645-86c3-5f73874e95e6,2ca5f3b0-0547-ac45-a458-6025395e62fd,257f1e59-a3ac-db44-89f6-602e0cf9d740,08fa04b6-5fd5-4840-a924-60f14c167429,ee3e6057-dd33-124e-bf82-6119a3f86ff6,ad1be26b-3841-5344-82d6-61f867ed409c,028bbdf2-9cb6-f348-8541-623086867976,795aca73-9330-8c4b-beb4-6242bc93b252,411bd7c6-e295-5943-8bfd-6263e64a087f,1e4e67ab-822b-144b-9437-62768f62e1b5,8ed2cf67-883e-554c-8efb-62d57dc7e396,488ad757-4c8d-8046-b721-6377e21379c9,d5f6c18a-c1e3-bd4f-bbb6-63bf21840832,e7d3f74c-3afb-1447-931c-63ede29e5bd2,f313869d-8e9a-9846-85d9-64e4489bab3f,335136b5-d5dd-e34b-ad56-64e513dcaab3,a83d600c-10e4-ab42-9f7b-6541905556bb,f4f6e075-5ea4-354a-b339-658102ff3035,5874ab55-2baf-6348-9c92-660058d9bdf5,fef52cdc-8d1e-e14a-9e00-661a2beaedfc,dd71acce-5d26-1545-8aee-667c8c180269,88b6ac81-b10c-6341-88ca-687ec0b8b781,24521cc2-1ddb-4742-9736-69329cccab26,56e6187f-21c2-7f49-a6f1-693b40d7ae38,1cfa4f6e-49c4-aa40-bc78-695e0ffd5125,a35be585-1806-474c-a3b6-69958d80cc07,a6dfb48a-fa04-ec4c-a087-6a00bbdec74e,02059dcf-87e1-4241-840d-6a5fa45c64b4,a49ddae3-b59c-1949-8b71-6a6c60eb3b72,8d08fe5f-56b9-2b49-bcaf-6b3ecf55896d,255a850a-d26a-2f44-9c63-6b73be6ef386,8abcfcd9-1c38-cb44-bf91-6c4ac9b0a87f,e432d410-df56-f649-82e3-6c4b26e86e4f,90e325a1-ad9b-914f-8901-6c95a657ba5d,bd4f2ca6-4c90-9843-872b-6d9bccbb89dc,bcc05233-5314-e849-b439-6d9edda54956,aaf74227-f9c0-a445-9ff6-6f0c6b638587,a913e226-e967-2d48-9976-6f2f89a0ed71,7ce878ce-b363-f34b-ba3d-70b8f0ac480e,8f74aa56-cb7a-544c-b701-71a5629543ba,57751463-c983-ab49-adac-71e4aae7b585,624b90c4-e437-0b4c-ab2c-72408c54cc15,5bc043e0-cb5c-bb4d-827e-728ca4e96653,e6f9123e-e21f-644f-8982-72b1f4132960,f5a3df9c-49f7-cf46-a0af-7399a0499b1a,21f501ff-e4c1-ef46-9d53-7415fa57e196,d4e4333d-d99b-1449-9df9-742bc0f92664,d7d1b63e-467c-524d-9472-742d3c045c08,c1902d18-634d-6140-acb1-745df1905b61,a0e5eafe-da69-9448-9222-74912289b49f,682a474b-0b88-0b46-a480-74a70e0b6423,338ba13f-2c11-7d4c-85ec-751fbef9212b,51c59af9-cfe3-4149-83c6-754b08d74579,450baa8b-45b8-c749-8016-754e97e2edbe,93ddcb46-d64f-aa46-9f0c-759d18abc2ca,cbf5e5c1-81e7-794d-984d-75a811fbb276,3bd72336-d210-4041-bbe6-765701492eed,af13b7fa-1054-6340-84f6-769b2c1836ac,6f57dbff-d226-5b44-b46f-76a72b36e085,15448691-381a-944d-8228-76d633241ac3,609d32a6-36b7-f942-9f3a-7778c0a06e34,69efa954-bcdd-f44e-a518-77ae6074791a,7485cb4f-df9d-5a42-828a-78021981ed54,1a269603-2763-944c-bf44-787763a1b5d6,43c91ac8-685e-3a44-991b-78c001435eff,f0a098b4-a1a4-d343-9b0e-79d846c58198,02be7bcf-82e2-724f-a6ef-7a19a580b93d,f035192d-d762-a149-8f58-7ae6f62bb42f,7282298b-3d8c-4d4d-a0a8-7af18bea26e5,e037c087-fa65-ad4f-841e-7cc7f274714d,99b2b6cd-9f78-f346-b06c-7ce4624f9ce8,8da2f14b-b529-2246-abca-7d22cc0e9a9d,9bb1cd7d-e9d1-6a4e-8bf7-7d5317795e93,80908352-653b-4e42-a102-7d71c1964709,b7edb46a-9442-614e-96d1-7db096ec8cef,1eaa914e-967f-4d4c-8935-7fd050d05b10,169e8ea4-11cd-1e4c-a87c-80a0a0fa888c,39a0da39-8094-0d4c-9214-80ccc7a4a1a6,99fdcf14-af6d-7246-b085-80ef23fd58a8,a1edd629-9b53-854e-a287-81256c97f1de,f5286221-c1d8-e14e-8d30-813a96049953,a95cda65-2ec5-284a-8154-81d10c7f68a0,9556f903-2545-8349-baf7-842151176191,6545fcd8-8d32-f14c-aa94-84224b7cd758,5958995d-54de-9945-a186-842b82d338fe,a2ee717d-52a6-2041-998b-84ffcd5476fd,515a8dc4-0bb2-d140-bc58-85ac3fb63a78,b048d4ac-eedf-8b49-a98b-86023b9ac62b,eb3ef4e2-0e57-244a-a185-862046598b35,a8836329-69f3-594b-ac48-863848603932,8640d484-424f-1a46-bc8c-8689b023c75a,601c90a4-dac2-c54b-8f55-869866df057a,79a2cd04-c151-ca41-b28f-86e432dff8cf,919dde35-8b1d-e54b-9d34-870b7e6c6b80,d6baa02b-6710-9941-af6f-875ed3b315d2,f7a8950b-042d-e44d-a6bc-876e38a1d925,13b721b8-cee6-db48-ab23-885e72b9e601,8e904f62-61a0-9140-9a58-897ed80c2640,dda6763d-518e-5940-b68d-89948723ff44,745b492d-2d82-3048-98fe-8a25a23fdb90,f9986a69-e3f2-8a45-b309-8a4956fd8b80,2ab7c98b-90ed-9f49-8b80-8b4db86dc1c9,2b47bc99-8722-2940-bee5-8b82828c460c,b4107126-8892-2642-ac1c-8c3c80963a91,c25c6356-6b84-3a4a-882f-8cf080a9278b,98bafa4c-3806-fb48-b5b4-8d701b2dd766,8d818bb9-8ebb-f64a-b220-8e05bf8e85cd,4ff43218-0906-114b-ba5c-8ee59d5dc99e,f13cc0c5-1ecc-0044-a979-8f5acfea6404,87e3d0ef-4b21-b84f-8e07-8f71a024f43d,cbeea6bd-8261-914d-9175-8fe1bbf29f94,0b5c2549-68e6-8141-9559-9051e32e0b27,1866f40a-22fb-6d47-b8d4-916ebcba9e1c,2d0b1e46-e3b5-3442-b79e-921d0b107983,77c9b697-1f3e-4248-857a-932fd0589300,5ea6f575-22c8-414e-9ef2-9426bfccbce3,585ccf72-8a5d-ba47-b9fd-943d27bdb2fb,93311140-d021-3846-ad55-943f504d857c,59da891b-b966-6e43-8c3b-949b90bb9376,87e260b7-a8df-434a-9bc1-94f344df9732,ad6c0662-065f-ca44-94ed-94fd77a53c8a,c07b5a09-3ac6-2c4e-9575-951faaeb62a8,d57471a0-1d9b-4d4e-8bcd-956a4684931d,9aa2b83f-e794-bf45-a45c-97bbdaacf148,a3293475-282c-dd4f-b23b-982d34534d75,862088cc-53c9-1241-9479-9867d44cebe9,2cd810f0-115c-2d42-ac33-98e63fdf6acc,14e91ccd-1f3b-3949-b3b7-9937e4ad6e30,475adbf4-5b08-6a40-b881-99a19bd74cb1,aa8c65fe-38c0-364e-a29b-9b122205120d,5e1b2871-5258-a744-bd72-9b72b3de06fd,9344826f-19c5-804f-8e5f-9c1679caa036,ca634d23-5abc-fa4b-ae41-9c760b503e73,17995778-b0fa-7843-b5f4-9cd74523bd89,9fe08c95-0266-1c4a-9d90-9ce3dc8fda99,a25d87e1-8907-1049-be88-9d351e7f6b73,0839dc5a-0e7b-6944-a71f-9deb206d3f68,eba604bc-bb9e-dd4b-82fa-9e8d98665be0,31ddb5a6-a51d-c549-8467-9efca6a5b3a5,abb3fc79-5c15-4648-9c5d-9f71258f726d,e1dc81bd-068b-6b47-8756-9f9b7ade56a1,4cc764ed-66ff-624f-a331-9fbf60796431,a0e70760-318a-734a-b011-a06eaa20a7d5,f3c371b9-ac15-494c-82b1-a07893a73d3b,5ac3ba68-2aaf-cc41-867c-a179a85c3225,8a365be0-3715-bb47-834d-a193736e51b9,894eab72-0f0d-244a-b9af-a1baada6f196,ac4962fb-96aa-db4c-9771-a2f6b24798ae,45bc97ab-e013-4780-ba66-a314a5caa079,acbd4275-df6a-cd4a-b944-a344c9a5bc9b,3540c0cd-4c46-c34c-bf1c-a389ce119aee,4fcf7344-0f24-4349-9d0c-a4a7dfac96d5,5da35b48-a5df-5949-8546-a4a812be8f5a,fba3d97e-3dfe-ec49-abe7-a4f61d5fe20b,5b0e08ac-f49d-d64d-9b32-a5932432af3b,7fe69d3b-a565-7d49-bc6a-a69789bed92b,a22af413-9073-6b42-b371-a6c3cec86fe7,2f6c3617-1c20-ba4a-bdae-a6e96e37865a,173e8f0e-087b-694c-850a-a6ed3eae0628,83faa87a-c1ed-344c-b792-a77289146ba8,b8359b24-d34a-904d-88c3-a83bb6da7fb9,33d6356e-8810-da49-a2f3-a91e0b351b22,fbc25a40-1614-0a43-9d9c-a92cf7d07ec9,bbb7f60c-3e90-bd41-a608-a93b7254018c,b60b456e-6d40-3144-b79e-a96cb76006b3,ad999e4f-6747-444d-a422-aa62af39dab6,879fd4b3-0b6b-3044-87b4-aa7129f33500,b2316151-d6fa-e449-abbc-aafbbe012d05,9c2cad2d-e7fd-6148-b0d3-ab11fa7b0439,d947d2ad-6bdf-1d44-8b17-abd69c8afb74,00ae4c5a-41a2-0947-83e2-ac253a7c9bab,fba356f8-ad6b-aa48-bd4f-ad02886bcb66,a9882b90-6cac-634f-b893-ad3bddf5a075,e01c88d0-3ddb-a84f-ad61-ad89e5f9c176,aeb9c6fd-3f77-5d4d-8113-ada931cde70c,ecdaf485-ea33-5d44-b62b-aeac595faee1,8de2ff41-2188-0b49-9a6c-af004f46c2f2,aa0a8c5e-2e5e-eb41-9fd2-b0481e1ab4f0,76de7fa0-91a4-db49-a7c9-b05a49200066,c54dccbb-461c-6c43-8ba4-b0a53d86a592,36d2fab3-516b-d64b-b9a4-b0de2996bed8,9645cb4a-7f7b-4b4f-9ad0-b3216acd009e,506055e6-e897-ec45-84f5-b3a394df71a5,8911c01b-a378-5b45-9427-b3d82349aeca,bb37eb8b-171d-d847-927d-b3f9c7b26dec,29591c68-a0ca-ed4e-90bb-b44f7e924d3f,c87b8c75-1d8e-9544-9cce-b45c9d0f1230,d32e11b1-5c1a-c842-b14f-b470e67443de,e7675aeb-d13b-fe4f-98ef-b534af46d187,0b744aef-a9dd-4e49-b65b-b54437cbed60,986ca0b4-648e-5247-99a6-b59693de5715,076c4d8d-6c41-464c-82d0-b62f602f7140,f088a5a8-f004-804e-b642-b6d42d8fa37e,9fecb195-5760-764f-8c22-b7340c8ec277,cbb3a136-da67-f54c-af77-b75ae0b24401,3b5c3177-462d-9c48-9e32-b7fbc8f64680,a359b9fd-c302-4b4b-a431-b805b8161fc2,02856125-383c-f546-944d-b82e986d997a,013da202-dc36-d34d-8bf8-b86dba2d9eff,18090e07-bd02-1047-8fd4-b933c49d900d,6266ea0a-f0e1-bb4d-97fa-b97aad03c427,84eb7fbe-8b63-ed4c-ab5c-b999423dd4eb,89751e29-4835-3a40-adc4-b9a7990d411a,3347a499-4471-b44e-94fe-ba3280a08652,47f0e3aa-8076-3f4f-ab6c-ba5456b084ce,5c51648e-8514-3941-b945-ba5e9e99bb89,de479d35-f793-3343-b9ea-bb119371ca19,ee6db14f-6c35-5640-92e0-bb32f7f8a504,74acc905-4a49-dc4f-8aae-bbc21c356616,6a460d32-7bb7-e74c-886f-bbdf9c50d6bf,9bdf76e0-ba2d-2f4d-981b-bc29d572ce1c,5a949e1d-676e-5b4f-9249-bc83a14880c6,d723fe7b-2577-6b44-88ed-bcaed09627af,0b8f4c6f-e03a-b744-af7b-bd4bc60ca5ea,4a498133-46dd-7944-ac0f-bdbeae9c3b3e,2787c360-4003-3147-ae21-bddfcbabf065,f051480d-caeb-a046-a7e9-be03d649f402,abbe7f93-cc3c-f54b-866c-be3fd5400331,49522f6c-b7d3-e846-8107-be9278c90d65,d5a66093-92dd-7c4a-9cb3-bea9b0db71fb,b19630cc-b6da-474d-93c2-bec2cdfd958f,686ce8c3-728d-894e-9e17-bece290a69cd,f5001645-bf10-4e4e-91f3-bf5be3c7ff88,496baf51-ca2a-2345-bf34-bf8bec602c63,05dd950d-a3e4-464c-9db7-bfc9049b0d96,3abd5f7c-74c5-274f-b717-c06daf97b822,c6a67cc0-554c-bb41-8bed-c08e6f9385ab,e04d0bfa-9330-d24d-a451-c0fdbc302fcd,01aae959-f540-9442-a08c-c110483fbb62,5d525b69-f5c2-2344-b416-c1275737a122,e6d30a8c-55ba-a341-b0e3-c14fb730741c,6960e9c0-87ec-7147-8c18-c18d30dadbf5,9edbab90-e8bc-6f41-97e4-c25e8e21c1a6,7ef2ce28-fcf9-e64d-a3b5-c29151d71cf5,9815cea0-755a-f449-8cfa-c2a21eebe113,39b4f245-2814-8547-ac6b-c3bf6e6af5ce,f2dd7bcf-b7ab-0e42-b0d7-c426dd613176,0bd82220-ab39-8146-9910-c465294eff57,4f294364-b12d-a448-bbc4-c473c9a0fbbb,b6fcdfee-e71f-d748-aae3-c5d3caf52690,96f9c3fe-5e44-a349-94d0-c5fe8c531a95,d037b5de-45a5-fa46-a950-c625ce368a48,2ba230f0-7648-6744-8c5c-c6a37686154a,c6b1094e-7d94-8546-99c4-c75bc1920c55,de970e0c-a550-c54b-b01f-c76cddd085fd,319f2593-0c77-1e44-8731-c7b114765256,caf41343-335f-ff49-9144-c7bc65fe225c,abd98638-fc80-0a4c-9870-c7eb79647765,993c7c27-ec44-6442-a12f-c80b6eeec233,7403ce91-622e-b747-9b0a-c836dc49e06b,25ad3809-da38-4a4a-a702-c84397e9a288,fa9e24e2-f399-8c4b-ac00-c87e46f0e5dc,3e7c2a69-9bd7-f645-9ab9-c8c72fec1b70,0a9c53a3-0092-6c4d-b6b4-c8d3b2028c34,34824a11-af80-694e-8b72-c9267d1b4fad,beefd96f-bddb-d247-bbe8-c94a047bc80f,1ebb35d6-f8ec-3443-a51d-c95cac5e249f,1303ccdb-8ee5-b241-be52-c9607327f0f4,a51de03c-83e6-3f40-9b5f-ca59398c7a15,79456b99-fced-7943-890a-ca86fe15e293,2c60bab4-1e9c-e341-af07-cb00ac606968,ae1ababe-5798-5b41-8a76-cb2c96ed4217,9c20da1c-af12-3c45-a83f-cb3a5aba05de,004e7bd9-85d4-2144-aa1f-cb4ece226081,4c3af7d1-f5f8-0c4f-9a8e-cb9591c2a379,9600bf4a-42c8-a349-9bba-cbc7d1840111,c9135fd5-b6e7-b441-80e1-cc270fe8f025,6e3b9346-c32e-f043-a871-cc9216f8beae,37ff0bdc-98fb-6e43-883c-cc9df253e8e1,056ca9b7-ac2e-fe4f-abc5-ccaae1e52787,d9841c6f-423d-1d4d-8502-cd58cfe3c67c,c826ced2-be81-214d-9617-cd6f3fd5d01f,403c15bc-63bb-d44b-9bfc-cd7cb8695afc,a0951771-8d81-0841-b768-cebcd74dd78b,ad599fcf-700e-5841-a224-cf71880310bc,d307d39e-f421-a247-899d-d00a8c8422be,7559d3fe-3b20-fa4c-b5e0-d1365a273fa7,9a9ac36d-b1a0-1c47-b8e4-d13a12d39a77,6f5eaee9-8d28-8341-9415-d15226d92353,9924b361-53d3-b449-850d-d1b9ccf1ca85,c50cc86a-9b59-4849-ae68-d1fe19171bac,0a1c547b-05dd-d941-bd9b-d2894422cd2c,2d60b79a-99aa-0549-aacd-d2cc7e304821,6b5bda55-8e66-d342-ba8e-d30b4006c2d9,a4c7cbe4-4893-834a-9960-d31a55d76f25,743c9654-7ee9-6145-b6eb-d39fcec583d0,f60d87cb-d9a2-1a4e-bf1d-d53c46a13a4f,cd55e205-be88-c14b-8dd2-d55a3f3d2af1,2b10fa5b-5ccc-374a-812b-d74a09109b5c,134365e6-026e-2d4b-a737-d75e85a675cf,316ff5d1-f10f-cd4f-88c5-d8444c2a211c,abb388f2-a099-864e-ab3c-d86bda539ea3,5649fb81-e11e-124e-b185-d87d58f6650e,7c8564e8-f14b-2343-a3f0-d9767ce309fa,faf9639e-2360-c94b-961c-d9b4817519d1,e432666b-c3f6-2645-9105-da7c271d5109,8ec24b16-8109-3c45-a74e-db64de17267b,387b82a0-17f9-4d46-a892-db67a50e764e,7f6a8d70-d020-4d43-a8ac-dbb33cbba8d0,c864efc4-2a87-4149-890e-dc09048d0bd5,d7dc1dd8-4eb2-284b-843e-dcee840877b9,5fadaa11-9c82-c947-80c2-de7c236019ee,05ed4692-73dd-7a4d-a8c9-deb2574ed0e6,ec339f80-2f91-b14c-b3b1-df044af04c3d,4848b642-9e48-bf4c-8371-df6e3954416e,fc4569ff-585f-594e-91da-dfcfd41821c6,e13a04e0-27ff-3042-bcde-e0389421bd8d,54a77a4a-bab4-df44-8e27-e069f0e0c5d3,5ba7a7ef-4384-b64e-936e-e0728bc89033,d59126fe-bd8d-0b4c-8681-e075ff2b31b9,e97682e1-b876-7d4d-9582-e0d9ede152dd,9abc723f-2b53-0540-bc68-e0ff14946dae,41cac78f-3405-7240-819a-e15001d4bb01,6f82ce61-777e-3c4e-b48a-e17325a1da0c,8129b9b4-027d-8e4d-b3fb-e175ea49e345,a92d814d-327a-4240-80af-e24cb25ae0e5,80f1aa3f-2d7d-ac42-a79b-e24d4771cfbc,b5a0c710-9a52-7a45-b72c-e2cb1b8af2c7,f0b369ed-a2c0-bd49-9073-e2cc6b7ed041,fc234f82-6b08-a74c-a5db-e315b4dcb2e8,aaba34db-0297-f74a-a041-e34e0dc647bb,2ffbcaf5-f383-3442-819c-e41d32434f4c,6ff2dd5d-5fd7-fa4f-9d9b-e49dc5558671,ad6fdf8f-2845-ca4b-a5a9-e4e45464cc59,ae882ff1-8702-4647-9a7c-e55cdaa7c991,cbe9557c-acb1-2c46-9600-e6896d159d75,4c29dc41-f8fe-1440-8f18-e70f5676098c,d9d7398b-b5a1-5245-acf2-e7f626492752,7173fdf1-ded9-294e-95f7-e80d017641bb,1d31d2d4-775f-064d-bd8a-e8219bbf094c,13e0ac9c-61cc-6842-91b6-e90a43c16ff8,5e377924-048d-8d40-91ec-e939d2441252,fa333ee9-73e3-f644-9588-e9543a313ada,d8e6582d-195d-0f46-883a-e97f72cdbcea,21d0c2b7-db86-c748-a271-e998cde3151c,73195bba-7267-e343-bc59-e9bac6ae91f3,fae11266-3f13-4248-97fd-ea1a00c70912,2283b002-25bf-a84d-9a30-eaa909316e18,250724eb-4308-8248-a535-eba9c4059593,98ab30f8-f100-e743-a48f-ec05f54824a2,75e46114-d982-4448-941c-ec6ea893dddd,2b5eacd8-e260-7c47-b48c-ed14f9d3a955,41e9a83b-a5b8-a647-951c-ed298b604495,b8a291e3-91e8-f941-ad0d-edac0220e15d,509b374b-281f-8140-aa73-edea792ab82d,4aa70cf0-558c-7f43-ab97-ee4d3b2955ea,f6e08149-c289-0e46-8a72-ee830f69b251,023633dd-bfd5-1945-8b2e-eebabd9ca862,255c9d90-2b82-ab42-84ce-eeffe81d9d03,9fb40504-7db1-714e-9860-ef2c75ffa4eb,c44547d8-f22f-b546-88ce-ef4932a34370,4a2c69ef-4d1b-6c48-9b3a-f01991e67251,59126e71-a4ba-2f43-a7e1-f04132edcea5,bd9f7620-b88a-f544-832d-f0487b209df9,9f34069a-be9a-1140-a92e-f0af83d7dda3,5d92b38e-de36-cb40-8a50-f0c7e66aa877,91b27aed-8b61-c945-a6ee-f0cedb008726,ea8bd078-93f3-b54a-a5a5-f13647e9d3db,e93617b4-472d-434f-a7a1-f1378f3238fd,03963ef4-5fb1-e44c-a62a-f2186494c59f,3d2438cb-48d8-cb49-b397-f2810ecf47d5,62eedd66-a580-9744-b657-f33a69d852dd,5a830633-18e7-1342-9aa0-f36f111f83b8,22928fef-e978-6d4b-8683-f3b002ef0fef,c0f9f24a-45ac-fa48-bc77-f3ede340bc2f,b6c48fd7-fec9-6e46-a382-f44342cfcf32,b3289566-d1ac-0c4c-b04d-f4e1ec117f26,052b2a12-da28-a44f-b73d-f4f4b5ac7dc1,24e0318c-db02-6745-8a2a-f52183bcbaa2,9aa092cc-95bf-7d41-81a5-f5222a6f3e72,b534117c-9539-f44e-be6e-f544cfb56bc2,7b332828-ce8c-6248-b317-f6df87a19b6f,323543d1-376a-3743-941f-f70f7922e722,038810d6-2da9-3745-a69d-f72e1fe854cc,e55958d0-2f43-4c41-b00b-f7707170c96b,05e99c4a-b262-a541-8c20-f7d55f974f91,06c53357-0ab4-9c49-ac37-f81acced638b,10f13e38-59a7-c746-ada7-f85b63431255,1449894f-10fe-e14e-8835-f94867074be6,e8dff73a-7ed1-bf44-a9ae-f975f370a2fa,f464606b-86b6-4640-89d3-f9af3e7dd97c,b30f8e7b-ca7f-ab4b-997a-f9f81ddf02f1,0dca97ef-6913-2f4f-83de-fae30aeee6ab,fd6d9b5d-c788-e843-9da9-fb29334f09ca,8de451e5-7b06-cf40-a939-fb9f7b19e39a,d95616f5-02b6-0645-8781-fbf2ef7ad474,a58770d0-6b82-8b44-bcdf-fbf7f179746e,e8e3dae9-2a3a-654d-b149-fc983f607d12,6d000a19-fce0-bf4c-a872-fcdca6591a2d,bca40d19-d300-df43-aa73-fdc7ce97b961,035e301a-a6c2-1a41-84fc-fed05cd5957b,efc60f82-a2b4-cd45-b167-fefa5d277fb2,7e3f9d19-85c1-8942-9874-ff09b11e1f18,65306923-691f-ef44-84c9-ff8dc98607dd,b6f1a9d5-e2db-db40-b780-ffb6012ed43b,'
-- =============================================
*/
CREATE PROCEDURE [dbo].[Proc_SA_SoTheoDoiThanhToanBangNgoaiTe] @FromDate DATETIME,
                                                               @ToDate DATETIME,
                                                               @TypeMoney NVARCHAR(3),
                                                               @Account NVARCHAR(25),
                                                               @AccountObjectID AS NVARCHAR(MAX),
                                                               @PhienLamViec INT,
                                                               @CompanyID UNIQUEIDENTIFIER,
                                                               @Dependent BIT
AS
BEGIN
    --lay company
    DECLARE @tblListCompanyID TABLE
                                  (
                                      CompanyID UNIQUEIDENTIFIER
                                  )
        IF (@Dependent = 1)
            BEGIN
                INSERT INTO @tblListCompanyID
                SELECT ID
                FROM EbOrganizationUnit
                WHERE (ID = @CompanyID)
                   OR (ParentID = @CompanyID AND UnitType = 1 AND AccType = 0)
            END
        ELSE
            BEGIN
                INSERT INTO @tblListCompanyID
                SELECT ID
                FROM EbOrganizationUnit
                WHERE ID = @CompanyID
            END

    --lay danh sach cac @AccountObjectID duoc chon
    DECLARE @tbAccountObjectID TABLE
                               (
                                   AccountingObjectID UNIQUEIDENTIFIER
                               )

    INSERT INTO @tbAccountObjectID
    SELECT tblAccOjectSelect.Value as AccountingObjectID
    FROM dbo.Func_ConvertStringIntoTable_Nvarchar(@AccountObjectID, ',') tblAccOjectSelect
    WHERE tblAccOjectSelect.Value in (SELECT ID FROM AccountingObject)

-- 	tao bang return
    DECLARE @tbDataReturn TABLE
                          (
                              stt                  INT,
                              IdGroup              smallint,
                              AccountingObjectID   UNIQUEIDENTIFIER,
                              AccountingObjectCode nvarchar(512),
                              AccountingObjectName nvarchar(512),
                              Account              nvarchar(25),
                              NgayHoachToan        Date,--PostedDate
                              NgayChungTu          Date,--Date
                              SoChungTu            nvarchar(25),--No
                              DienGiai             nvarchar(512), --Description
                              TKDoiUng             nvarchar(512),--AccountCorresponding
                              TyGiaHoiDoai         decimal(25, 10),--ExchangeRate
                              PSNSoTien            money,--DebitAmountOriginal
                              PSNQuyDoi            money, --DebitAmount
                              PSCSoTien            money, --CreditAmountOriginal
                              PSCQuyDoi            money, --CreditAmount
                              DuNoSoTien           money,
                              DuNoQuyDoi           money,
                              DuCoSoTien           money,
                              DuCoQuyDoi           money,
                              SDDKSoTien           money,
                              SDDKQuyDoi           money,
                              TonSoTien            money,--so du ton cong don so tien
                              TonQuyDoi            money,--so du ton cong don quy doi
                              OrderPriority        int,
                              RefID                UNIQUEIDENTIFIER,
                              RefType              int
                          )


--lay so du dau ky
    DECLARE @tbDataDauKy TABLE
                         (
                             AccountingObjectID UNIQUEIDENTIFIER,
                             Account            nvarchar(25),
                             SoDuDauKySoTien    money,
                             SoDuDauKyQuyDoi    money
                         )

    INSERT INTO @tbDataDauKy(AccountingObjectID, Account, SoDuDauKySoTien, SoDuDauKyQuyDoi)
    SELECT GL.AccountingObjectID,
           GL.Account,
           SUM(GL.DebitAmountOriginal - GL.CreditAmountOriginal) as SoDuDauKySoTien,
           SUM(GL.DebitAmount - GL.CreditAmount)                 as SoDuDauKyQuyDoi
    FROM GeneralLedger GL
    WHERE (GL.AccountingObjectID in (Select AccountingObjectID from @tbAccountObjectID))
      AND GL.PostedDate < @FromDate
      AND GL.CurrencyID = @TypeMoney
      AND GL.Account LIKE (@Account + '%')
      AND GL.CompanyID in (select D.CompanyID from @tblListCompanyID D)
    GROUP BY GL.AccountingObjectID, GL.Account

-- --lay du lieu tu GL
--     DECLARE @IdGroup1 smallint
--     set @IdGroup1 = 5;

    INSERT INTO @tbDataReturn(stt, AccountingObjectID, AccountingObjectCode, Account, NgayHoachToan,
                              NgayChungTu, SoChungTu, DienGiai,
                              TKDoiUng, TyGiaHoiDoai, PSNSoTien, PSNQuyDoi, PSCSoTien, PSCQuyDoi, OrderPriority,
                              RefID, RefType)
    SELECT ROW_NUMBER() OVER (ORDER BY GL.Account, GL.AccountingObjectCode, GL.Date, GL.PostedDate,
        CASE WHEN @PhienLamViec = 0 THEN GL.NoFBook ELSE GL.NoMBook END, GL.AccountCorresponding)   as stt,
--            @IdGroup1                                                       as IdGroup,
           GL.AccountingObjectID,
           GL.AccountingObjectCode,
           Gl.Account,
           GL.PostedDate                                                   as NgayHoachToan,
           GL.Date                                                         as NgayChungTu,
           CASE WHEN @PhienLamViec = 0 THEN GL.NoFBook ELSE GL.NoMBook END as SoChungTu,
           GL.Description                                                  as DienGiai,
           GL.AccountCorresponding                                         as TKDoiUng,
           GL.ExchangeRate                                                 as TyGiaHoiDoai,
           GL.DebitAmountOriginal                                          as PSNSoTien,
           GL.DebitAmount                                                  as PSNQuyDoi,
           GL.CreditAmountOriginal                                         as PSCSoTien,
           Gl.CreditAmount                                                 as PSCQuyDoi,
           GL.OrderPriority                                                as OrderPriority,
           GL.ReferenceID                                                  as RefID,
           GL.TypeID                                                       as RefType
    FROM GeneralLedger GL
    WHERE (GL.PostedDate Between @FromDate And @ToDate)
      AND (GL.CurrencyID = @TypeMoney)
      AND (GL.AccountingObjectID in (Select AccountingObjectID from @tbAccountObjectID))
      AND GL.Account like (@Account + '%')
      AND GL.CompanyID in (select D.CompanyID from @tblListCompanyID D)

--insert so du dau ky vao, ke ca cac tai khoan ko co phat sinh trong ky nay vao @tbDataReturn
    DECLARE @stt1 smallint
    set @stt1 = 0;

    Declare @SoDuDauKySoTien money
    Set @SoDuDauKySoTien = 0
    Declare @SoDuDauKyQuyDoi money
    Set @SoDuDauKyQuyDoi = 0

    INSERT INTO @tbDataReturn(stt, AccountingObjectID, Account, DienGiai, TonSoTien, TonQuyDoi)
    SELECT @stt1,
--            @IdGroup1,
           AccountingObjectID,
           Account,
           '',
           @SoDuDauKySoTien,
           @SoDuDauKyQuyDoi
    FROM @tbDataReturn
    GROUP BY AccountingObjectID, Account

--Update so du dau ky vao @tbDataReturn {Neu da co AccountingObjectID, Account thi Update, nguoc lai thi insert}
    DECLARE @iIDDauKy UNIQUEIDENTIFIER
    DECLARE @iAccountDauKy NVARCHAR(25)
    DECLARE @iTonSoTienDauKy money
    DECLARE @iTonQuyDoiDauKy money

    DECLARE cursorDauKy CURSOR FOR
        SELECT AccountingObjectID, Account, SoDuDauKySoTien, SoDuDauKyQuyDoi FROM @tbDataDauKy
    OPEN cursorDauKy
    FETCH NEXT FROM cursorDauKy INTO @iIDDauKy, @iAccountDauKy, @iTonSoTienDauKy, @iTonQuyDoiDauKy
    WHILE @@FETCH_STATUS = 0
        BEGIN
            Declare @countCheck int
            set @countCheck = (select count(AccountingObjectID)
                               from @tbDataReturn
                               where AccountingObjectID = @iIDDauKy
                                 and Account = @iAccountDauKy)

            If (@countCheck > 0)
                Begin
                    Update @tbDataReturn
                    Set TonSoTien  = @iTonSoTienDauKy,
                        TonQuyDoi  = @iTonQuyDoiDauKy,
                        SDDKSoTien = @iTonSoTienDauKy,
                        SDDKQuyDoi = @iTonQuyDoiDauKy
                    where AccountingObjectID = @iIDDauKy
                      and Account = @iAccountDauKy
                End
            Else
                Begin
                    Insert Into @tbDataReturn(stt, AccountingObjectID, Account, DienGiai, TonSoTien,
                                              TonQuyDoi, SDDKSoTien, SDDKQuyDoi)
                    Values (@stt1, @iIDDauKy, @iAccountDauKy, '', @iTonSoTienDauKy,
                            @iTonQuyDoiDauKy, @iTonSoTienDauKy, @iTonQuyDoiDauKy)
                End
            FETCH NEXT FROM cursorDauKy INTO @iIDDauKy, @iAccountDauKy, @iTonSoTienDauKy, @iTonQuyDoiDauKy
        END
    CLOSE cursorDauKy
    DEALLOCATE cursorDauKy

-- su dung contro duyet tung dong va tinh gia tri so du luy ke
    DECLARE @iID UNIQUEIDENTIFIER
    DECLARE @iAccount NVARCHAR(25)
    DECLARE @iTonSoTien money
    DECLARE @iTonQuyDoi money

    DECLARE cursorTon CURSOR FOR
        SELECT AccountingObjectID, Account, TonSoTien, TonQuyDoi FROM @tbDataReturn WHERE stt = 0
    OPEN cursorTon
    FETCH NEXT FROM cursorTon INTO @iID, @iAccount, @iTonSoTien, @iTonQuyDoi
    WHILE @@FETCH_STATUS = 0
        BEGIN
            Declare @tonSoTien money
            set @tonSoTien = ISNULL(@iTonSoTien, 0)
            Declare @tonQuyDoi money
            set @tonQuyDoi = ISNULL(@iTonQuyDoi, 0)

            Declare @minStt bigint
            set @minStt =
                    (select MIN(stt)
                     from @tbDataReturn
                     where AccountingObjectID = @iID
                       and Account = @iAccount
                      and stt > 0)
            Declare @maxStt bigint
            set @maxStt =
                    (select MAX(stt)
                     from @tbDataReturn
                     where AccountingObjectID = @iID
                       and Account = @iAccount
                       and stt > 0)
            WHILE @minStt <= @maxStt
                Begin
                    set @tonSoTien = @tonSoTien +
                                     ISNULL((select PSNSoTien from @tbDataReturn where stt = @minStt), 0) -
                                     ISNULL((select PSCSoTien from @tbDataReturn where stt = @minStt), 0)
                    set @tonQuyDoi = @tonQuyDoi +
                                     ISNULL((select PSNQuyDoi from @tbDataReturn where stt = @minStt), 0) -
                                     ISNULL((select PSCQuyDoi from @tbDataReturn where stt = @minStt), 0)
                    UPDATE @tbDataReturn SET TonSoTien = @tonSoTien, TonQuyDoi = @tonQuyDoi, IdGroup = @maxStt WHERE stt = @minStt
                    set @minStt = @minStt + 1
                End
            FETCH NEXT FROM cursorTon INTO @iID, @iAccount, @iTonSoTien, @iTonQuyDoi
        END
    CLOSE cursorTon
    DEALLOCATE cursorTon

--     DECLARE @iID1 UNIQUEIDENTIFIER
--     DECLARE @iAccount1 NVARCHAR(25)
--
--     DECLARE cursorTon1 CURSOR FOR
--         SELECT AccountingObjectID, Account FROM @tbDataReturn WHERE stt = 0
--     OPEN cursorTon1
--     FETCH NEXT FROM cursorTon1 INTO @iID1, @iAccount1
--     WHILE @@FETCH_STATUS = 0
--         BEGIN
--             Declare @maxStt1 bigint
--             set @maxStt1 = (select MAX(stt)
--                             from @tbDataReturn
--                             where AccountingObjectID = @iID1
--                               and Account = @iAccount1
--                               and stt > 0)
--             If (@maxStt1 > 0)
--                 Begin
--                     UPDATE @tbDataReturn
--                     SET TonSoTien = (select TonSoTien from @tbDataReturn where stt = @maxStt1)
--                       , TonQuyDoi = (select TonQuyDoi from @tbDataReturn where stt = @maxStt1)
--                     WHERE AccountingObjectID = @iID1
--                       AND Account = @iAccount1
--                       AND IdGroup = 3
--                 End
--             Else
--                 Begin
--                     UPDATE @tbDataReturn
--                     SET TonSoTien = (select TonSoTien
--                                      from @tbDataReturn
--                                      where stt = 0
--                                        and AccountingObjectID = @iID1
--                                        and Account = @iAccount1)
--                       , TonQuyDoi = (select TonQuyDoi
--                                      from @tbDataReturn
--                                      where stt = 0
--                                        and AccountingObjectID = @iID1
--                                        and Account = @iAccount1)
--                     WHERE AccountingObjectID = @iID1
--                       AND Account = @iAccount1
--                       AND IdGroup = 3
--                 End
--             FETCH NEXT FROM cursorTon1 INTO @iID1, @iAccount1
--         END
--     CLOSE cursorTon1
--     DEALLOCATE cursorTon1

    UPDATE @tbDataReturn SET DuNoSoTien = TonSoTien WHERE TonSoTien > 0
    UPDATE @tbDataReturn SET DuCoSoTien = TonSoTien WHERE TonSoTien < 0

    UPDATE @tbDataReturn SET DuNoQuyDoi = TonQuyDoi WHERE TonQuyDoi > 0
    UPDATE @tbDataReturn SET DuCoQuyDoi = TonQuyDoi WHERE TonQuyDoi < 0

    UPDATE @tbDataReturn SET DuCoSoTien = (-1 * DuCoSoTien) WHERE DuCoSoTien < 0
    UPDATE @tbDataReturn SET DuCoQuyDoi = (-1 * DuCoQuyDoi) WHERE DuCoQuyDoi < 0

    UPDATE @tbDataReturn
    SET AccountingObjectName = AJ.AccountingObjectName,
        AccountingObjectCode = AJ.AccountingObjectCode
    FROM (select ID, AccountingObjectCode, AccountingObjectName from AccountingObject) AJ
    where AccountingObjectID = AJ.ID

    SELECT stt
         , IdGroup
         , AccountingObjectID
         , AccountingObjectCode
         , AccountingObjectName
         , Account
         , NgayHoachToan
         , NgayChungTu
         , SoChungTu
         , DienGiai
         , TKDoiUng
         , TyGiaHoiDoai
         , PSNSoTien
         , PSNQuyDoi
         , PSCSoTien
         , PSCQuyDoi
         , DuNoSoTien
         , DuNoQuyDoi
         , DuCoSoTien
         , DuCoQuyDoi
         , SDDKSoTien
         , SDDKQuyDoi
         , RefID
         , RefType
    FROM @tbDataReturn
    WHERE (ISNULL(PSNSoTien, 0) > 0)
       OR (ISNULL(PSNQuyDoi, 0) > 0)
       OR (ISNULL(PSCSoTien, 0) > 0)
       OR (ISNULL(PSCQuyDoi, 0) > 0)
       OR (ISNULL(DuNoSoTien, 0) > 0)
       OR (ISNULL(DuNoQuyDoi, 0) > 0)
       OR (ISNULL(DuCoSoTien, 0) > 0)
       OR (ISNULL(DuCoQuyDoi, 0) > 0)
    ORDER BY Account, AccountingObjectCode, NgayChungTu, NgayHoachToan, SoChungTu,TKDoiUng ,OrderPriority, IdGroup
END
go

