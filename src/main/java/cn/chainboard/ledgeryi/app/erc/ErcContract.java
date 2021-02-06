package cn.chainboard.ledgeryi.app.erc;

public class ErcContract {

    public static final String ERC721 =
            "// SPDX-License-Identifier: GPL-3.0\n" +
            "pragma solidity ^0.6.8;\n" +
            "\n" +
            "contract ERC721Enumerable {\n" +
            "\n" +
            "    string internal nftName;\n" +
            "    string internal nftSymbol;\n" +
            "    address internal creator;\n" +
            "\n" +
            "    mapping(uint256 => bool) internal burned;\n" +
            "    mapping(uint256 => address) internal owners;\n" +
            "    mapping(address => uint256) internal balances;\n" +
            "\n" +
            "    uint256 internal maxId;\n" +
            "    uint[] internal tokenIndexes;\n" +
            "    mapping(uint => uint) internal indexTokens;\n" +
            "    mapping(uint => uint) internal tokenTokenIndexes;\n" +
            "    mapping(address => uint[]) internal ownerTokenIndexes;\n" +
            "\n" +
            "    constructor(string memory name_, string memory symbol_, uint _initialSupply) public {\n" +
            "        creator = msg.sender;\n" +
            "        nftName = name_;\n" +
            "        nftSymbol = symbol_;\n" +
            "        maxId = _initialSupply;\n" +
            "        balances[msg.sender] = _initialSupply;\n" +
            "        for(uint i = 0; i < _initialSupply; i++){\n" +
            "            tokenTokenIndexes[i+1] = i;\n" +
            "            ownerTokenIndexes[creator].push(i+1);\n" +
            "            tokenIndexes.push(i+1);\n" +
            "            indexTokens[i+1] = i;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    function isValidToken(uint256 _tokenId) public view returns (bool){\n" +
            "        return _tokenId != 0 && _tokenId <= maxId && !burned[_tokenId];\n" +
            "    }\n" +
            "\n" +
            "    function balanceOf(address _owner) public view returns (uint256){\n" +
            "        return balances[_owner];\n" +
            "    }\n" +
            "\n" +
            "    function ownerOf(uint256 _tokenId) public view returns (address){\n" +
            "        require(isValidToken(_tokenId));\n" +
            "        if(owners[_tokenId] != address(0) ){\n" +
            "            return owners[_tokenId];\n" +
            "        }else{\n" +
            "            return creator;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    function totalSupply() public view returns (uint256){\n" +
            "        return tokenIndexes.length;\n" +
            "    }\n" +
            "\n" +
            "    function tokenByIndex(uint256 _index) public view returns(uint256){\n" +
            "        require(_index < tokenIndexes.length);\n" +
            "        return tokenIndexes[_index];\n" +
            "    }\n" +
            "\n" +
            "    function tokenOfOwnerByIndex(address _owner, uint256 _index) public view returns (uint256){\n" +
            "        require(_index < balances[_owner]);\n" +
            "        return ownerTokenIndexes[_owner][_index];\n" +
            "    }\n" +
            "\n" +
            "    function transferFrom(address _from, address _to, uint256 _tokenId) public {\n" +
            "        address owner = ownerOf(_tokenId);\n" +
            "        require (owner == msg.sender);\n" +
            "        require(owner == _from);\n" +
            "        require(_to != address(0));\n" +
            "        require(isValidToken(_tokenId));\n" +
            "        owners[_tokenId] = _to;\n" +
            "        balances[_from]--;\n" +
            "        balances[_to]++;\n" +
            "        uint oldIndex = tokenTokenIndexes[_tokenId];\n" +
            "        if(oldIndex != ownerTokenIndexes[_from].length - 1){\n" +
            "            ownerTokenIndexes[_from][oldIndex] = ownerTokenIndexes[_from][ownerTokenIndexes[_from].length - 1];\n" +
            "            tokenTokenIndexes[ownerTokenIndexes[_from][oldIndex]] = oldIndex;\n" +
            "        }\n" +
            "        ownerTokenIndexes[_from].pop();\n" +
            "        tokenTokenIndexes[_tokenId] = ownerTokenIndexes[_to].length;\n" +
            "        ownerTokenIndexes[_to].push(_tokenId);\n" +
            "    }\n" +
            "\n" +
            "    function issueTokens(uint256 _extraTokens) public{\n" +
            "        require(msg.sender == creator);\n" +
            "        balances[msg.sender] = balances[msg.sender] + (_extraTokens);\n" +
            "        uint thisId;\n" +
            "        for(uint i = 0; i < _extraTokens; i++){\n" +
            "            thisId = maxId + i + 1;\n" +
            "            tokenTokenIndexes[thisId] = ownerTokenIndexes[creator].length;\n" +
            "            ownerTokenIndexes[creator].push(thisId);\n" +
            "            indexTokens[thisId] = tokenIndexes.length;\n" +
            "            tokenIndexes.push(thisId);\n" +
            "        }\n" +
            "        maxId = maxId + _extraTokens;\n" +
            "    }\n" +
            "\n" +
            "    function burnToken(uint256 _tokenId) public{\n" +
            "        address owner = ownerOf(_tokenId);\n" +
            "        require(owner == msg.sender);\n" +
            "        burned[_tokenId] = true;\n" +
            "        balances[owner]--;\n" +
            "        uint oldIndex = tokenTokenIndexes[_tokenId];\n" +
            "        if(oldIndex != ownerTokenIndexes[owner].length - 1){\n" +
            "            ownerTokenIndexes[owner][oldIndex] = ownerTokenIndexes[owner][ownerTokenIndexes[owner].length - 1];\n" +
            "            tokenTokenIndexes[ownerTokenIndexes[owner][oldIndex]] = oldIndex;\n" +
            "        }\n" +
            "        ownerTokenIndexes[owner].pop();\n" +
            "        delete tokenTokenIndexes[_tokenId];\n" +
            "        oldIndex = indexTokens[_tokenId];\n" +
            "        if(oldIndex != tokenIndexes.length - 1){\n" +
            "            tokenIndexes[oldIndex] = tokenIndexes[tokenIndexes.length - 1];\n" +
            "            indexTokens[ tokenIndexes[oldIndex] ] = oldIndex;\n" +
            "        }\n" +
            "        tokenIndexes.pop();\n" +
            "        delete indexTokens[_tokenId];\n" +
            "    }\n" +
            "\n" +
            "    function name() external view returns (string memory _name) {\n" +
            "        _name = nftName;\n" +
            "    }\n" +
            "\n" +
            "    function symbol() external view returns (string memory _symbol){\n" +
            "        _symbol = nftSymbol;\n" +
            "    }\n" +
            "}";

    public static final String ERC20 =
            "// SPDX-License-Identifier: GPL-3.0\n" +
            "pragma solidity ^0.6.9;\n" +
            "contract BasicFT {\n" +
            "\n" +
            "    string public name;\n" +
            "    string public symbol;  \n" +
            "    uint256 public totalSupply;\n" +
            "\n" +
            "    event Transfer(address indexed from, address indexed to, uint tokens);\n" +
            "    event Burn(address indexed account, uint256 amount);\n" +
            "    event Mint(address indexed account, uint256 amount);\n" +
            "\n" +
            "    mapping(address => uint256) balances;\n" +
            "\n" +
            "    constructor(string memory name_, string memory symbol_, uint256 totalSupply_) public {\n" +
            "        balances[msg.sender] = totalSupply_;\n" +
            "        name = name_;\n" +
            "        symbol = symbol_;\n" +
            "        totalSupply = totalSupply_;\n" +
            "    }\n" +
            "    \n" +
            "    function balanceOf(address tokenOwner) public view returns (uint) {\n" +
            "        return balances[tokenOwner];\n" +
            "    }\n" +
            "\n" +
            "    function burn(address account, uint256 amount) public {\n" +
            "        require(amount <= balances[account]);\n" +
            "        balances[account] = sub(balances[account], amount);\n" +
            "        totalSupply = sub(totalSupply, amount);\n" +
            "        emit Burn(account, amount);\n" +
            "    }\n" +
            "\n" +
            "    function mint(address account, uint256 amount) public {\n" +
            "        balances[account] = add(balances[account], amount);\n" +
            "        totalSupply = add(totalSupply, amount);\n" +
            "        emit Mint(account, amount);\n" +
            "    }\n" +
            "\n" +
            "    function transfer(address receiver, uint numTokens) public returns (bool) {\n" +
            "        require(numTokens <= balances[msg.sender]);\n" +
            "        balances[msg.sender] = sub(balances[msg.sender] , numTokens);\n" +
            "        balances[receiver] = add(balances[receiver], numTokens);\n" +
            "        emit Transfer(msg.sender, receiver, numTokens);\n" +
            "        return true;\n" +
            "    }\n" +
            "\n" +
            "    function sub(uint256 a, uint256 b) internal pure returns (uint256) {\n" +
            "      assert(b <= a);\n" +
            "      return a - b;\n" +
            "    }\n" +
            "    \n" +
            "    function add(uint256 a, uint256 b) internal pure returns (uint256) {\n" +
            "      uint256 c = a + b;\n" +
            "      assert(c >= a);\n" +
            "      return c;\n" +
            "    }\n" +
            "\n" +
            "}";
}
